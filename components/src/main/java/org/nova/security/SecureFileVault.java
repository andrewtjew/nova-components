package org.nova.security;

import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.nova.core.Utils;

// Using FileVault:
// Secret file should contain one line entries of format: key=value
// Encrypt secret file using the encrypt method.
// Use FileVault to read entries

public class SecureFileVault extends Vault
{
	public static final void encrypt(String password,String salt,String inputFileName, String outputFileName) throws Exception
	{
		String text=Utils.readTextFile(inputFileName);
		byte[] byteText = text.getBytes();

		SecretKey secretKey = buildKey(password,salt);
		Cipher AesCipher = Cipher.getInstance("AES");
		AesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

		try (FileOutputStream writer=new FileOutputStream(outputFileName))
		{
			writer.write(AesCipher.doFinal(byteText));
		}
	}

	static private SecretKey buildKey(String password,String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 256);
		SecretKey secretKey=factory.generateSecret(spec);
		return new SecretKeySpec(secretKey.getEncoded(),"AES");
	}
	
	public static final String decrypt(String password,String salt,String inputFileName) throws Exception
	{
		byte[] bytes=Utils.readFile(inputFileName);
		SecretKey secretKey = buildKey(password,salt);
		Cipher AesCipher = Cipher.getInstance("AES");
		AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] bytePlainText = AesCipher.doFinal(bytes);
		return new String(bytePlainText);
	}

	private final HashMap<String, byte[]> map;
	private final SecretKey secretKey;
	public SecureFileVault(String password,String salt,String inputFileName) throws Exception
	{
		this.secretKey=buildKey(password, salt);
		this.map=new HashMap<>();
		
		String text=decrypt(password,salt,inputFileName);
		String[] lines=text.split("\n");
		for (String line:lines)
		{
		    int index=line.indexOf('=');
			if (index<0)
			{
				continue;
			}
			String key=line.substring(0, index).trim();
			String value=line.substring(index+1).trim();
			
			Cipher AesCipher = Cipher.getInstance("AES");
			AesCipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
			byte[] byteText = value.getBytes();
			byte[] byteCipherText = AesCipher.doFinal(byteText);
			this.map.put(key.trim(), byteCipherText);
		}
	}

	public String get(String key) throws Exception 
	{
		byte[] bytes=this.map.get(key);
		if (bytes==null)
		{
		    return null;
		}
		Cipher AesCipher = Cipher.getInstance("AES");
		AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] bytePlainText = AesCipher.doFinal(bytes);
		return new String(bytePlainText).trim();
	}

}
