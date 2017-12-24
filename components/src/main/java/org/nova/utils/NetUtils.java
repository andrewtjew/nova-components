package org.nova.utils;

import java.net.NetworkInterface;

public class NetUtils
{
    public static String getLocalHostName() throws Exception
    {
        return java.net.InetAddress.getLocalHost().getHostName();
    }

    public static String getMacAddress() throws Exception
    {
        NetworkInterface network = NetworkInterface.getByInetAddress(java.net.InetAddress.getLocalHost());
        if (network==null)
        {
            return null;
        }
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X", mac[i]));
        }
        return sb.toString();
    }

}
