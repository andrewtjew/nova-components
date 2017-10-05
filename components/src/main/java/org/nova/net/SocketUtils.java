package org.nova.net;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.nova.core.Utils;
import org.nova.frameworks.ServerApplication;

public class SocketUtils
{
    /*
    public static KeyManager[] getKeyManagers(ServerApplication application) throws Exception
    {
        String keyStorePath = application.getConfiguration().getValue("KeyManager.keyStorePath");
        if (keyStorePath!= null)
        {
            String keyStorePassword = application.getVault().get("KeyManager.keyStorePassword");
            String keyManagerPassword = application.getVault().get("KeyManager.password");
            String algorithm = application.getConfiguration().getValue("KeyManager.algorithm", KeyManagerFactory.getDefaultAlgorithm());
            return getKeyManagers(keyStorePassword, keyStorePath, keyManagerPassword, algorithm);
        }
        return null;
    }
    */

    public static KeyManager[] getKeyManagers(String keyStorePassword, String keyStorePath, String keyManagerPassword, String algorithm) throws Exception
    {
        if (keyStorePath!= null)
        {
            if (algorithm == null)
            {
                algorithm = KeyManagerFactory.getDefaultAlgorithm();
            }

            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream inputStream = new FileInputStream(Utils.toNativePath(keyStorePath)))
            {
                keyStore.load(inputStream, keyStorePassword.toCharArray());
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
            keyManagerFactory.init(keyStore, keyManagerPassword.toCharArray());
            return keyManagerFactory.getKeyManagers();
        }
        return null;
    }

    /*
    public static TrustManager[] getTrustManagers(ServerApplication application) throws Exception
    {
        String keyStorePath = application.getConfiguration().getValue("TrustManager.keyStorePath");
        if (keyStorePath!= null)
        {
            String keyStorePassword = application.getVault().get("TrustManager.keyStorePassword");
            String algorithm = application.getConfiguration().getValue("TrustManager.algorithm", TrustManagerFactory.getDefaultAlgorithm());
            return getTrustManagers(keyStorePassword, keyStorePath, algorithm);
        }
        return getTrustAllManagers();
    }
    */

    public static TrustManager[] getTrustManagers(String keyStorePassword, String keyStorePath, String algorithm) throws Exception
    {
        if (keyStorePath!= null)
        {
            if (algorithm == null)
            {
                algorithm = KeyManagerFactory.getDefaultAlgorithm();
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream inputStream = new FileInputStream(Utils.toNativePath(keyStorePath)))
            {
                keyStore.load(inputStream, keyStorePassword.toCharArray());
            }
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        }
        return getTrustAllManagers();
    }

    public static TrustManager[] getTrustAllManagers()
    {
        return new TrustManager[]
        {
            new X509TrustManager() 
            {
                public X509Certificate[] getAcceptedIssuers() 
                {
                    return null;
                }
    
                public void checkClientTrusted(X509Certificate[] certs, String authType) 
                {
                }
    
                public void checkServerTrusted(X509Certificate[] certs, String authType) 
                {
                }
            }
        };
    }
}
