package org.nova.http.client;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import java.security.cert.Certificate;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.CertificateUtils;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.nova.core.Utils;

public class HttpClientFactory
{
    static public HttpClient createDefaultClient()
    {
        RequestConfig config=RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        HttpClients.custom().setDefaultRequestConfig(config);
        PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(10);
        connectionManager.setMaxTotal(10);
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    static private boolean verify(String name, SSLSession session)
    {
        try
        {
            for (Certificate certificate:session.getPeerCertificates())
            {
                if (certificate instanceof X509Certificate)
                {
                    X509Certificate x509=(X509Certificate)certificate;
                    Collection<List<?>> alternativeNames=x509.getSubjectAlternativeNames();
                    if (alternativeNames!=null)
                    {
                        for (List<?> list:x509.getSubjectAlternativeNames())
                        {
                            for (Object object:list)
                            {
                              //TODO: need to generate a certificate with alternate cn  
                            }
                        }
                    }
                    else
                    {
                        Principal principal=x509.getIssuerX500Principal();
                        if (principal!=null)
                        {
                            String principalName=principal.getName();
                            String[] keyValues=Utils.split(principalName, ',');
                            for (String keyValue:keyValues)
                            {
                                String[] pair=Utils.split(keyValue, '=');
                                if (pair.length==2)
                                {
                                    if ("CN".equals(pair[0]))
                                    {
                                        if (name.equals(pair[1]))
                                        {
                                            return true;
                                        }
                                    }
                                            
                                }
                                
                            }
                        }
                    }
                }
            }
            return false;
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    static class ClusterNameVerifier implements HostnameVerifier
    {
        final private String clusterName;
        
        public ClusterNameVerifier(String clusterName)
        {
            this.clusterName=clusterName;
        }
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return HttpClientFactory.verify(this.clusterName, session);
        }
        
    }
    
    final static HostnameVerifier STRICT_HOSTNAME_VERIFIER=new HostnameVerifier()
    {
        
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return HttpClientFactory.verify(hostname, session);
        }
    };
    
    static public HttpClient createSSLClient(HttpClientConfiguration configuration,String clientCertficateStorePath, String clientCertficateStorePassword,String serverCertificateStorePath,String serverCertificateStorePassword,String clusterName) throws Throwable
    {
        KeyStore clientCertficateStore=CertificateUtils.getKeyStore(new PathResource(new File(Utils.toLocalFileName(clientCertficateStorePath))), "JKS", null, clientCertficateStorePassword);
        KeyStore serverCertificateStore=CertificateUtils.getKeyStore(new PathResource(new File(Utils.toLocalFileName(serverCertificateStorePath))), "JKS", null, serverCertificateStorePassword);
        SSLContextBuilder contextBuilder=new SSLContextBuilder().
                loadKeyMaterial(clientCertficateStore,clientCertficateStorePassword.toCharArray()).  
                loadTrustMaterial(serverCertificateStore,null);
        HostnameVerifier verifier=clusterName!=null?new ClusterNameVerifier(clusterName):STRICT_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory connectionSocketFactory=new SSLConnectionSocketFactory(contextBuilder.build(),new String[]{"TLSv1.2"},null,verifier);

        RequestConfig config=RequestConfig.custom().setSocketTimeout(configuration.socketTimeout).setConnectTimeout(configuration.connectionTimeout).build();
        HttpClients.custom().setDefaultRequestConfig(config);
        
        return HttpClients.custom().setSSLSocketFactory(connectionSocketFactory).build();
    }
    
}
