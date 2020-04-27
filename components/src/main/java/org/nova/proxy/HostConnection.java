package org.nova.proxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceRunnable;

class HostConnection implements TraceRunnable
{
    final InsideServer server;
    final private long created;
    final private int outputPort;
    final private OutputStream outputStream;
    private Socket socket;
    
    public HostConnection(InsideServer server,int outsidePort) throws Throwable
    {
        this.created=System.currentTimeMillis();
        this.server=server;
        this.outputPort=outsidePort;

        InsideConfiguration configuration=this.server.getConfiguration();
        this.socket=new Socket(configuration.hostEndPoint,configuration.hostPort);
        socket.setReceiveBufferSize(configuration.hostReceiveBufferSize);
        socket.setSendBufferSize(configuration.hostSendBufferSize);
        socket.setTcpNoDelay(true);
        this.outputStream=socket.getOutputStream();
    }
    @Override
    public void run(Trace parent) throws Throwable
    {
        try
        {
            InsideConfiguration configuration=this.server.getConfiguration();
            InputStream inputStream=socket.getInputStream();
            Packet hostPacket=new Packet(configuration.hostReceiveBufferSize, this.outputPort);

            for (;;)
            {
                int read=hostPacket.readFromStream(inputStream);
                System.out.println("HostConnection: port="+this.outputPort+", read from host="+read);
                if (read<0)
                {
                    break;
                }
                if (read>0)
                {
                    this.server.sendToProxy(hostPacket);
                    System.out.println("HostConnection: port="+this.outputPort+", sent to proxy="+hostPacket.getDataSize());
                }
            }
            
        }
        finally
        {
            this.server.removeHostConnection(this.outputPort);
            close();
        }
    }

    public void writeToHost(Packet proxyPacket) throws Throwable
    {
        synchronized(this)
        {
            //this.outputStream cannot be null. If so, then implementation error.
            proxyPacket.writeToStream(this.outputStream);
            System.out.println("HostConnection: port="+this.outputPort+", sent to host="+proxyPacket.getDataSize());
        }
    }
    
    public void close()
    {
        try
        {
            synchronized(this)
            {
                if (this.socket!=null)
                {
                    this.socket.close();
                    this.socket=null;
                }
            }
        }
        catch (Throwable t)
        {
            this.server.getLogger().log(t);
            synchronized(this)
            {
                this.socket=null;
            }
        }
    }
}