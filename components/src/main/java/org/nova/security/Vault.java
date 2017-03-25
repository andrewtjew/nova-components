package org.nova.security;

public abstract class Vault 
{
	public abstract String get(String key) throws Exception;
}