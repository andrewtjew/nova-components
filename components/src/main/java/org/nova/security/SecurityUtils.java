package org.nova.security;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.nova.utils.TypeUtils;

public class SecurityUtils
{
    public static byte[] computeHashSHA256(byte[] data) throws Throwable
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(data, 0, data.length);
        return sha256.digest();
    }
    
    public static byte[] computeHashSHA256(int[] data,int dataBlockSize) throws Throwable
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        int index=0;
        byte[] buffer=new byte[dataBlockSize*4];
        while (index<data.length)
        {
            if (data.length-index<dataBlockSize)
            {
                dataBlockSize=data.length-index;
            }
            TypeUtils.toBytesLittleEndian(data, buffer,index,0,dataBlockSize);
            sha256.update(buffer, 0, buffer.length);
        }
            
        return sha256.digest();
    }
    
    public static final byte[] encrypt(String password,String salt,byte[] data) throws Exception
    {
        SecretKey secretKey = buildKey(password,salt);
        Cipher AesCipher = Cipher.getInstance("AES");
        AesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return AesCipher.doFinal(data);
    }

    static private SecretKey buildKey(String password,String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 256);
        SecretKey secretKey=factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(),"AES");
    }
    
    public static byte[] decrypt(String password,String salt,byte[] bytes) throws Exception
    {
        SecretKey secretKey = buildKey(password,salt);
        Cipher AesCipher = Cipher.getInstance("AES");
        AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        return AesCipher.doFinal(bytes);
    }
    
}
