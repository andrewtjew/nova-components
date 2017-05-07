package org.nova.flow;

import java.util.ArrayList;
import java.util.LinkedList;

import org.nova.concurrent.Synchronization;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.test.Testing;

public class SourceQueue<ITEM>
{
    final private CountMeter droppedMeter;
    final private CountMeter stalledMeter;
    final private LevelMeter waitingMeter;
    final private int sendSizeThreshold;
    final private long flushWait;
    final private long sendWait;
    final private int stallSizeThreshold;
    final private long stallWait;
    final private long endSegmentWait;
    final private int maxQueueSize;

    final private Receiver receiver;
    final private Object lock;
    private Thread thread;

    private ArrayList<Packet> buffer;
    private Packet current;

    private Throwable throwable;
    private boolean stop;
    private boolean noWait = false;

    public SourceQueue(Receiver receiver, SourceQueueConfiguration configuration)
    {
        this.droppedMeter = new CountMeter();
        this.stalledMeter = new CountMeter();
        this.waitingMeter = new LevelMeter();
        this.receiver = receiver;
        this.sendSizeThreshold = configuration.sendSizeThreshold;
        this.flushWait = configuration.flushWait;
        this.sendWait = configuration.sendWait;
        this.stallSizeThreshold = configuration.stallSizeThreshold;
        this.stallWait = configuration.stallWait;
        this.endSegmentWait = configuration.rollOverWait;
        this.maxQueueSize = configuration.maxQueueSize;

        this.lock = new Object();
        this.buffer = new ArrayList<>();
        this.current = new Packet(this.sendSizeThreshold);
    }

    public void start()
    {
        synchronized (this.lock)
        {
            if (this.thread == null)
            {
                Testing.setTimeBase(System.currentTimeMillis());
                this.stop = false;
                this.thread = new Thread(() ->
                {
                    main();
                });
                this.thread.start();
            }
        }
    }

    public void stop() throws InterruptedException
    {
        synchronized (this.lock)
        {
            if (this.thread != null)
            {
                this.stop = true;
                this.lock.notifyAll();
                this.thread.join();
                this.thread = null;
            }
        }
    }

    public void send(ITEM item)
    {
        synchronized (this.lock)
        {
            long size = this.waitingMeter.getLevel();
            if (size >= this.maxQueueSize)
            {
                this.droppedMeter.increment();
                return;
            }
            if (size >= this.stallSizeThreshold)
            {
                try
                {
                    this.lock.wait(this.stallWait);
                }
                catch (InterruptedException e)
                {
                }
                this.stalledMeter.increment();
            }
            if (this.stop == true)
            {
                return;
            }
            boolean added = this.current.add(item);
            if (added == false)
            {
                this.buffer.add(this.current);
                this.current = new Packet(this.sendSizeThreshold);
            }
            this.waitingMeter.increment();
            size = this.waitingMeter.getLevel(); // need latest.
            if ((size == 1) || (size == this.sendSizeThreshold))
            {
                this.lock.notify();
                Testing.oprintln("SourceQueue:notify, queue size=" + size);
            }
            else
            {
                Testing.oprintln("SourceQueue:send, queue size=" + size);
            }
        }
    }

    public void flush()
    {
        synchronized (this.lock)
        {
            if (this.stop == true)
            {
                return;
            }
            long size = this.waitingMeter.getLevel(); // need latest.
            if (size > 0)
            {
                this.buffer.add(this.current);
                this.current = new Packet(this.sendSizeThreshold);
            }
            this.buffer.add(Packet.FLUSH_PACKET);
            this.noWait = true;
            this.lock.notify();
        }
    }

    public void rollOver()
    {
        synchronized (this.lock)
        {
            if (this.stop == true)
            {
                return;
            }
            long size = this.waitingMeter.getLevel(); // need latest.
            if (size > 0)
            {
                this.buffer.add(this.current);
                this.current = new Packet(this.sendSizeThreshold);
            }
            this.buffer.add(null);
            this.noWait = true;
            this.lock.notify();
        }
    }

    private void main()
    {
        try
        {
            boolean flush = false;
            long lastRollOver = System.currentTimeMillis();
            this.receiver.beginSegment(lastRollOver);

            Testing.oprintln("SourceQueue:enter");
            for (;;)
            {
                ArrayList<Packet> toSendBuffer = null;
                synchronized (this.lock)
                {
                    if (this.waitingMeter.getLevel() < this.sendSizeThreshold)
                    {
                        long timeout = Long.MAX_VALUE;
                        long start = System.currentTimeMillis();
                        if (this.endSegmentWait > 0)
                        {
                            timeout = this.endSegmentWait - (start - lastRollOver);
                        }
                        if (this.flushWait > 0)
                        {
                            if (flush)
                            {
                                if (timeout > this.flushWait)
                                {
                                    timeout = this.flushWait;
                                }
                            }
                        }
                        Testing.oprintln("SourceQueue:start wait, timeout=" + timeout);
                        if (Synchronization.waitForNoThrow(this.lock, () ->
                        {
                            return this.waitingMeter.getLevel() > 0 || this.stop || this.noWait;
                        } , timeout))
                        {
                            Testing.oprintln("SourceQueue:end wait, queue size=" + this.waitingMeter.getLevel());
                            if (this.waitingMeter.getLevel() < this.sendSizeThreshold) 
                            {
                                long waited = System.currentTimeMillis() - start;
                                if (waited < this.sendWait)
                                {
                                    timeout = this.sendWait - waited;
                                    Testing.oprintln("SourceQueue:start wait for sendSizeThreshold, current queue size=" + waitingMeter.getLevel() + ", timeout=" + timeout);
                                    Synchronization.waitForNoThrow(this.lock, () ->
                                    {
                                        return this.waitingMeter.getLevel() >= this.sendSizeThreshold || this.stop;
                                    } , timeout);
                                    Testing.oprintln("SourceQueue:end wait for sendSizeThreshold");
                                }
                            }
                        }
                    }
                    if (this.stop)
                    {
                        return;
                    }
                    this.noWait = false;
                    if (this.waitingMeter.getLevel() > 0)
                    {
                        Testing.oprintln("SourceQueue:switch buffers");
                        toSendBuffer = this.buffer;
                        if (this.current.size() > 0)
                        {
                            toSendBuffer.add(this.current);
                            this.current = new Packet(this.sendSizeThreshold);
                        }
                        this.buffer = new ArrayList<>(toSendBuffer.size() * 2);
                        this.waitingMeter.set(0);
                    }
                }
                if (toSendBuffer != null)
                {
                    int sendSize = 0;
                    for (Packet packet : toSendBuffer)
                    {
                        if ((packet != null) && (packet.size() > 0))
                        {
                            sendSize += packet.size();
                        }
                    }
                    Packet sendPacket = new Packet(sendSize);
                    Testing.oprintln("SourceQueue:send size=" + sendSize);

                    for (Packet packet : toSendBuffer)
                    {
                        if (packet == null)
                        {
                            if (sendPacket.size() > 0)
                            {
                                this.receiver.send(sendPacket);
                                sendPacket = new Packet((int) this.waitingMeter.getLevel());
                            }
                            long rollOver = System.currentTimeMillis();
                            if (rollOver < lastRollOver)
                            {
                                rollOver = lastRollOver + 1;
                            }
                            lastRollOver = rollOver;
                            this.receiver.endSegment();
                            this.receiver.beginSegment(rollOver);
                            flush = false;
                        }
                        else if (packet.size() == Packet.FLUSH)
                        {
                            if (sendPacket.size() > 0)
                            {
                                this.receiver.send(sendPacket);
                                sendPacket = new Packet((int) this.waitingMeter.getLevel());
                            }
                            if (flush)
                            {
                                this.receiver.flush();
                            }
                            flush = false;
                        }
                        else
                        {
                            Object[] array = packet.get();
                            for (int i = 0; i < packet.size(); i++)
                            {
                                sendPacket.add(array[i]);
                            }
                            flush = true;
                        }
                    }
                    if (sendPacket.size() > 0)
                    {
                        this.receiver.send(sendPacket);
                        flush = true;
                    }
                }
                else if (flush)
                {
                    Testing.oprintln("SourceQueue:flush");
                    this.receiver.flush();
                    flush = false;
                }
                else if (this.endSegmentWait > 0)
                {
                    long rollOver = System.currentTimeMillis();
                    if (rollOver - lastRollOver >= this.endSegmentWait)
                    {
                        Testing.oprintln("SourceQueue:rollover");
                        this.receiver.endSegment();
                        this.receiver.beginSegment(rollOver);
                        lastRollOver = rollOver;
                    }
                }
            }
        }
        catch (Throwable t)
        {
            synchronized (this.lock)
            {
                this.throwable = t;
            }
        }
    }

    public Throwable getThrowable()
    {
        synchronized (this.lock)
        {
            return this.throwable;
        }
    }

    public Receiver getReceiver()
    {
        return this.receiver;
    }

    public CountMeter getDroppedMeter()
    {
        return droppedMeter;
    }

    public CountMeter getStalledMeter()
    {
        return stalledMeter;
    }

    public LevelMeter getWaitingMeter()
    {
        return waitingMeter;
    }
}
