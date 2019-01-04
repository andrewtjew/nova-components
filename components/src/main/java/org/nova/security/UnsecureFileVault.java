package org.nova.security;

import java.util.HashMap;

import org.nova.utils.FileUtils;

public class UnsecureFileVault extends Vault
{
	private final HashMap<String, String> map;

	public UnsecureFileVault(String inputFileName) throws Exception
	{
	    this.map=new HashMap<>();
		String text=FileUtils.readTextFile(inputFileName);
		String[] lines=text.split("\n");
		for (String line:lines)
		{
			if (line.indexOf("=")<0)
			{
				continue;
			}
			String[] keyValue=line.trim().split("=");
			String key=keyValue[0].trim();
			String value=keyValue[1].trim();
			this.map.put(key, value);
		}
	}

	public String get(String key) throws Exception 
	{
		return this.map.get(key);
	}

}
