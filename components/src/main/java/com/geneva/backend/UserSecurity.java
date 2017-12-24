package com.geneva.backend;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.nova.sqldb.Accessor;
import org.nova.sqldb.Connector;
import org.nova.sqldb.RowSet;
import org.nova.sqldb.Transaction;
import org.nova.tracing.Trace;

public class UserSecurity
{
	final Connector connector;
	final SecureRandom random;

	public UserSecurity(Connector connector) throws NoSuchAlgorithmException
	{
		this.connector=connector;
		this.random=new SecureRandom();
		SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); //Just to warm up things.
	}
	
	public AddUserStatus addUser(Trace parent,String user,String password) throws Throwable
	{
		byte[] salt=new byte[32];
		this.random.nextBytes(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = factory.generateSecret(spec).getEncoded();
		Base64.Encoder encoder = Base64.getEncoder();
		String saltHash=encoder.encodeToString(salt)+encoder.encodeToString(hash);

		try (Accessor accessor=connector.openAccessor(parent, "addUser"))
		{
			try (Transaction transaction=accessor.beginTransaction("addUserTransaction"))
			{
				if (accessor.executeQuery(parent, null, "SELECT user FROM user WHERE user=?",user).size()!=0)
				{
					transaction.rollback();
					return AddUserStatus.USER_EXISTS; 
				}
				accessor.executeUpdate(parent, null, "INSERT INTO id VALUES()");
				long id=accessor.executeQuery(parent, null, "SELECT LAST_INSERT_ID()").getRow(0).get(0);
				if (accessor.executeUpdate(parent, null, "INSERT INTO user (user,id_user) VALUES(?,?)",user,id)!=1)
				{
					transaction.rollback();
					throw new Exception();
				}
				if (accessor.executeUpdate(parent, null, "INSERT INTO security (id_user,hash) VALUES(?,?)",id,saltHash)!=1)
				{
					transaction.rollback();
					throw new Exception();
				}
				transaction.commit();
			}
		}
		return AddUserStatus.SUCCESS;
	}

	public boolean verifyUser(Trace parent,String user,String password) throws Throwable
	{
		String saltHashText;
		try (Accessor accessor=connector.openAccessor(parent, "verifyUser"))
		{
			RowSet rowSet=accessor.executeQuery(parent, null, "SELECT id_user FROM user WHERE user=?",user);
			if (rowSet.size()!=1)
			{
				return false; 
			}
			long id=rowSet.getRow(0).getBIGINT(0);
			rowSet=accessor.executeQuery(parent, null, "SELECT hash FROM security WHERE id_user=?",id);
			if (rowSet.size()!=1)
			{
				return false;
			}
			saltHashText=rowSet.getRow(0).getCHAR(0);
		}
		String saltText=saltHashText.substring(0,44);
		String hashText=saltHashText.substring(44);
		byte[] salt=Base64.getDecoder().decode(saltText);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		String clientHashText=Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
		return clientHashText.equals(hashText);
	}
	
	public boolean changePassword(Trace parent,String user,String password,String newPassword) throws Throwable
	{
		String saltHashText;
		long id;
		try (Accessor accessor=connector.openAccessor(parent, "verifyUser"))
		{
			RowSet rowSet=accessor.executeQuery(parent, null, "SELECT id_user FROM user WHERE user=?",user);
			if (rowSet.size()!=1)
			{
				return false; 
			}
			id=rowSet.getRow(0).getBIGINT(0);
			rowSet=accessor.executeQuery(parent, null, "SELECT hash FROM security WHERE id_user=?",id);
			if (rowSet.size()!=1)
			{
				return false;
			}
			saltHashText=rowSet.getRow(0).getCHAR(0);
		}
		String saltText=saltHashText.substring(0,44);
		String hashText=saltHashText.substring(44);
		byte[] salt=Base64.getDecoder().decode(saltText);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		String clientHashText=Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
		if (clientHashText.equals(hashText)==false)
		{
			return false;
		}

		byte[] newSalt=new byte[32];
		this.random.nextBytes(newSalt);
		spec = new PBEKeySpec(newPassword.toCharArray(), newSalt, 65536, 128);
		byte[] newHash = factory.generateSecret(spec).getEncoded();
		Base64.Encoder encoder = Base64.getEncoder();
		String newHashText=encoder.encodeToString(newHash);
		if (hashText.equals(newHashText)) 
		{
			return false; //Using the fact that hash collisions are very unlikely.
		}
		String newSaltHash=encoder.encodeToString(newSalt)+newHashText;
		
		try (Accessor accessor=connector.openAccessor(parent, "verifyUser"))
		{
			return accessor.executeUpdate(parent, null, "UPDATE security SET hash=? WHERE id_user=? AND hash=?",newSaltHash,id,saltHashText)==1;
		}
		
	}
	
	public boolean setPassword(Trace parent,String user,String newPassword) throws Throwable
	{
		long id;
		try (Accessor accessor=connector.openAccessor(parent, "setPassword.select"))
		{
			RowSet rowSet=accessor.executeQuery(parent, null, "SELECT id_user FROM user WHERE user=?",user);
			if (rowSet.size()!=1)
			{
				return false; 
			}
			id=rowSet.getRow(0).getBIGINT(0);
		}

		byte[] newSalt=new byte[32];
		this.random.nextBytes(newSalt);
		KeySpec spec = new PBEKeySpec(newPassword.toCharArray(), newSalt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] newHash = factory.generateSecret(spec).getEncoded();
		Base64.Encoder encoder = Base64.getEncoder();
		String newHashText=encoder.encodeToString(newHash);
		String newSaltHash=encoder.encodeToString(newSalt)+newHashText;

		try (Accessor accessor=connector.openAccessor(parent, "setPassword.update"))
		{
			return accessor.executeUpdate(parent, null, "UPDATE security SET hash=? WHERE id_user=?",newSaltHash,id)==1;
		}
		
	}
	
	public String generateSessionId(int bytes)
	{
		byte[] sessionId=new byte[bytes];
		this.random.nextBytes(sessionId);
		return Base64.getEncoder().encodeToString(sessionId);
	}
	
}
