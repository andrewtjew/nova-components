package org.nova.frameworks;

import org.apache.http.client.HttpClient;
import org.nova.http.client.HttpClientConfiguration;
import org.nova.http.client.HttpClientFactory;
import org.nova.http.client.JSONClient;

public class ServerApplicationUtils
{
    static class HttpClientEndPointConfiguration
    {
        public String clientCertificateStorePath;
        public String serverCertificateStorePath;
        public String clientCertificatePasswordKey;
        public String serverCertificatePasswordKey;
        public String endPoint;
        public String clusterName;
        public HttpClientConfiguration httpClientConfiguration;
    }
    
    
    public static JSONClient createJSONClient(ServerApplication application,String configurationName) throws Throwable
    {
        HttpClientEndPointConfiguration configuration=application.getConfiguration().getJSONObject(configurationName, null, HttpClientEndPointConfiguration.class);
        if (configuration==null)
        {
            return null;
        }
        HttpClient client;
        if (configuration.endPoint.startsWith("https://"))
        {
            String clientCertificateStorePassword=application.getVault().get(configuration.clientCertificatePasswordKey);
            String serverCertificateStorePassword=application.getVault().get(configuration.serverCertificatePasswordKey);
            client=HttpClientFactory.createSSLClient(configuration.httpClientConfiguration, configuration.clientCertificateStorePath, clientCertificateStorePassword, configuration.serverCertificateStorePath, serverCertificateStorePassword, configuration.clusterName);
        }
        else
        {
            client=HttpClientFactory.createClient(configuration.httpClientConfiguration);
        }

        
        return new JSONClient(application.getTraceManager(),application.getLogger(),configuration.endPoint,client);
    }
}
