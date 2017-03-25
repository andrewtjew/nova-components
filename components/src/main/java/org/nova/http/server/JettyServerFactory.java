package org.nova.http.server;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

public class JettyServerFactory
{
    static Server createServer(ThreadPool threadPool, int port)
    {
        Server server = new Server(threadPool);
        HttpConfiguration config = new HttpConfiguration();
        config.setOutputBufferSize(65536);
        ServerConnector connector = new ServerConnector(server,new HttpConnectionFactory(config));
        connector.setPort(port);
        server.addConnector(connector);
        return server;
    }

    static public Server createServer(int threads, int port)
    {
        return createServer(new ExecutorThreadPool(threads, threads, 0), port);
    }

    static public Server createHttpsServer(int threads, int port, String keyStorePath, String keyStorePassword,String trustStorePath,String trustStorePassword,String keyManagerPassword)
    {
        HttpConfiguration config=new HttpConfiguration();
        config.setOutputBufferSize(65536);
        config.setRequestHeaderSize(8192);
        config.setResponseHeaderSize(8192);
        config.setSecurePort(port);
        return createHttpsServer(new ExecutorThreadPool(threads, threads, 0),config,keyStorePath,keyStorePassword,trustStorePath,trustStorePassword,keyManagerPassword);
    }
    
    static public Server createHttpsServer(ThreadPool threadPool, HttpConfiguration config, String keyStorePath, String keyStorePassword,String trustStorePath,String trustStorePassword,String keyManagerPassword)
    {
        Server server = new Server(threadPool);
        config.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory();
        if (keyStorePath!=null)
        {
            sslContextFactory.setKeyStorePath(keyStorePath);
            sslContextFactory.setKeyStorePassword(keyStorePassword);
        }
        if (trustStorePath!=null)
        {
            sslContextFactory.setNeedClientAuth(true);
            sslContextFactory.setTrustStorePath(trustStorePath);
            sslContextFactory.setTrustStorePassword(trustStorePassword);
        }
        sslContextFactory.setKeyManagerPassword(keyManagerPassword);
 //       sslContextFactory.setTrustAll(true);
        sslContextFactory.setRenegotiationAllowed(false);
        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(config));
        sslConnector.setPort(config.getSecurePort());
        server.setConnectors(new Connector[]{sslConnector});
        
        
        return server;
    }
}