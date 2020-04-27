package org.nova.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.nova.concurrent.MultiTaskScheduler;
import org.nova.concurrent.TimeBase;
import org.nova.concurrent.TimerRunnable;
import org.nova.concurrent.TimerScheduler;
import org.nova.concurrent.TimerTask;
import org.nova.json.ObjectMapper;
import org.nova.logging.Logger;
import org.nova.test.Testing;
import org.nova.tracing.Trace;
import org.nova.utils.NetUtils;
import org.nova.utils.TypeUtils;

public class InsideServer
{
    final private MultiTaskScheduler scheduler;
    final private TimerScheduler timer;
    final private InsideConfiguration configuration;
    final private Logger logger;
    final HashMap<Integer,HostConnection> hostConnections;
    private InputStream proxyInputStream;
    private OutputStream proxyOutputStream;
    Packet keepAlivePacket;
    
    public InsideServer(MultiTaskScheduler scheduler,TimerScheduler timer,Logger logger,InsideConfiguration configuration)
    {
        this.configuration=configuration;
        this.scheduler=scheduler;
        this.logger=logger;
        this.timer=timer;
        this.keepAlivePacket=new Packet();
        this.hostConnections=new HashMap<>();
    }

    public void start() throws IOException
    {
        this.scheduler.schedule(null, "run", (trace)->{run(trace);});
        
    }
    
    public InsideConfiguration getConfiguration()
    {
        return this.configuration;
    }
    void read(byte[] buffer,int offset,int size) throws Throwable
    {
        int totalRead=0;
        while (totalRead!=size)
        {
            int read=this.proxyInputStream.read(buffer,offset+totalRead,size-totalRead);
            if (read<0)
            {
                throw new Exception();
            }
            totalRead+=read;
        }
    }
    
    
    public Logger getLogger()
    {
        return this.logger;
    }
    
    private void run(Trace parent) throws Throwable
    {
        synchronized(this)
        {
            this.proxyInputStream=null;
            this.proxyOutputStream=null;
        }
        this.timer.schedule("KeepAlive", TimeBase.FREE,1000,this.configuration.keepAliveInterval,(trace,event)->keepAlive());
        
        int reconnectDelay=this.configuration.reconnectDelay;
        for (;;)
        {
            System.out.println("Connecting to outside");
            try
            {
                try (Socket socket=new Socket(this.configuration.proxyEndPoint,this.configuration.proxyPort))
                {
                    socket.setReceiveBufferSize(this.configuration.proxyReceiveBufferSize);
                    socket.setSendBufferSize(this.configuration.proxySendBufferSize);
                    socket.setTcpNoDelay(true);
                    synchronized(this)
                    {
                        this.proxyInputStream=socket.getInputStream();
                        this.proxyOutputStream=socket.getOutputStream();
                    }
                    ProxyConfiguration proxyConfiguration=new ProxyConfiguration();
                    proxyConfiguration.outsideListenPort=this.configuration.outsideListenPort;
                    proxyConfiguration.insideName=NetUtils.getLocalHostName();
                    proxyConfiguration.insideMacAddress=NetUtils.getMacAddress();
                    String text=ObjectMapper.writeObjectToString(proxyConfiguration);
                    byte[] bytes=text.getBytes();
                    this.proxyOutputStream.write(TypeUtils.bigEndianIntToBytes(bytes.length));
                    this.proxyOutputStream.write(bytes);
                    
                    for (;;)
                    {
                        Packet proxyPacket=Packet.readFromProxyStream(this.proxyInputStream);
                        int dataSize=proxyPacket.getDataSize();
                        if (dataSize==0)
                        {
//                            System.out.println("Keepalive response received");
                            reconnectDelay=this.configuration.reconnectDelay;
                            continue;
                        }
                        int outsidePort=proxyPacket.getPort();
                        if (dataSize==4)
                        {
                            synchronized(this.hostConnections)
                            {
                                HostConnection connection=this.hostConnections.remove(outsidePort);
                                connection.close();
                                continue;
                            }
                        }
                        HostConnection connection;
                        synchronized(this.hostConnections)
                        {
                            connection=this.hostConnections.get(outsidePort);
                            if (connection==null)
                            {
                                connection=new HostConnection(this, outsidePort);
                                this.hostConnections.put(outsidePort, connection);
                                this.scheduler.schedule(parent, "HostConnection", connection);
                                System.out.println("Inside: new HostConnection, outsidePort="+outsidePort);
                            }
                            connection.writeToHost(proxyPacket);
                        }
                    }
                }
            }
            catch (Throwable t)
            {
                this.logger.log(t);
            }
            finally
            {
                synchronized(this)
                {
                    this.proxyOutputStream=null;
                }
                synchronized (this.hostConnections)
                {
                    for (HostConnection connection:this.hostConnections.values())
                    {
                        connection.close();
                    }
                    this.hostConnections.clear();
                }
                
            }
            System.out.println("Sleep before reconnect: "+reconnectDelay);
            Thread.sleep(reconnectDelay);
            
            reconnectDelay*=2;
            if (reconnectDelay>this.configuration.maxReconnectDelay)
            {
                reconnectDelay=this.configuration.maxReconnectDelay;
            }
        }
    }
    
    void removeHostConnection(int outsidePort) throws Throwable
    {
        HostConnection connection;
        synchronized (this.hostConnections)
        {
            connection=this.hostConnections.remove(outsidePort);
        }
        if (connection!=null)
        {
            connection.close();
        }
        closeOutside(outsidePort);
    }

    public void closeOutside(int outsidePort)
    {
        try
        {
            sendToProxy(new Packet(outsidePort));
        }
        catch (Throwable t)
        {
            this.getLogger().log(t);
        }
    }
    
    void sendToProxy(Packet hostPacket) throws Throwable
    {
        OutputStream outputStream;
        synchronized(this)
        {
            outputStream=this.proxyOutputStream;
        }
        hostPacket.writeToProxyStream(outputStream);
    }

    void keepAlive() throws Throwable
    {
        synchronized(this)
        {
            if (this.proxyOutputStream!=null)
            {
                this.keepAlivePacket.writeToProxyStream(this.proxyOutputStream);
                System.out.println("Keepalive sent");
            }
        }
    }
}
