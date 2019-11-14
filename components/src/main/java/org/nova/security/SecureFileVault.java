package org.nova.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.nova.configuration.Configuration;
import org.nova.utils.FileUtils;

// Using FileVault:
// Secret file should contain one line entries of format: key=value
// Encrypt secret file using the encrypt method.
// Use FileVault to read entries

public class SecureFileVault extends Vault
{
	public static final void encrypt(String password,String salt,String inputFileName, String outputFileName) throws Exception
	{
		String text=FileUtils.readTextFile(inputFileName);
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
		byte[] bytes=FileUtils.readFile(inputFileName);
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

	static private String makePasswordFile(String in)
	{
	    return FileUtils.toNativePath(in+"/map.cnf");
	}
	
	public static Vault getVault(Configuration configuration) throws Throwable
	{
	    try
	    {
            String secureVaultFile=configuration.getValue("Environment.Vault.secureVaultFile",null);
            if (secureVaultFile!=null)
            {
                PasswordMethod passwordMethod=configuration.getEnumValue("Environment.alert",PasswordMethod.class);
                String password=null;
                
                switch (passwordMethod)
                {
                    case system:
                        if (System.console()==null)
                        {
                            System.err.println("Error code: 8614");
                            System.exit(1);
                        }
                        password=new String(System.console().readPassword("Enter vault password:"));
                        break;
                    case high:
                    {
                        String passwordFile=makePasswordFile(configuration.getValue("Environment.preferences",null));
                        try
                        {
                            password=FileUtils.readTextFile(passwordFile).trim();
                        }
                        catch (Throwable t)
                        {
                            System.err.println("Error code: 6762");
                            System.exit(1);
                        }
                        try
                        {
                            if (new File(passwordFile).delete()==false)
                            {
                                System.err.println("Error code: 5367");
                                System.exit(1);
                            }
                        }
                        catch (Throwable t)
                        {
                            System.err.println("Error code: 5367");
                            System.exit(1);
                        }
                    }
                        break;
                    case normal:
                    {
                        String passwordFile=makePasswordFile(configuration.getValue("Environment.preferences",null));
                        if (passwordFile==null)
                        {
                            System.err.println("Error code: 4665");
                            System.exit(1);
                        }
                        try
                        {
                            password=FileUtils.readTextFile(passwordFile).trim();
                        }
                        catch (Throwable t)
                        {
                            System.err.println("Error code: 6762");
                            System.exit(1);
                        }
                    }
                        break;
                    default:
                        break;
                    
                }
                String salt=configuration.getValue("Environment.Vault.salt");
                return new SecureFileVault(password, salt, secureVaultFile);
            }
            else
            {
                printUnsecureVaultWarning(System.err);
                String unsecureVaultFile=configuration.getValue("Environment.Vault.unsecureVaultFile",FileUtils.toNativePath("./resources/UnSecureVault.cnf"));
                try
                {
                    return new UnsecureFileVault(unsecureVaultFile);
                }
                catch (Throwable t)
                {
                    System.out.println("Vault is empty.");
                    return new UnsecureFileVault();
                }
            }
	    }
	    finally
	    {
            configuration.remove("Environment.preferences");
            configuration.remove("Environment.Vault.secureVaultFile");
            configuration.remove("Environment.alert");
            configuration.remove("Environment.Vault.salt");
	    }
	    
	}
    private static void printUnsecureVaultWarning(PrintStream stream)
    {
        stream.println("**************************************");
        stream.println("**   WARNING: Using UnsecureVault   **");
        stream.println("**************************************");
    }
	
}
