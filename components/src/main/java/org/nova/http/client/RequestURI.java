package org.nova.http.client;

import java.util.HashMap;
import java.util.Map;

import org.nova.core.Utils;

public class RequestURI
{
	final String scheme;
	final String host;
	final String path;
	final int port;
	final Map<String,Integer> pathMap;
	final String[] parts;
	final String fragment;
	
	public RequestURI(String scheme,String host,int port,String path,String fragment) throws Exception
	{
		this.scheme=scheme;
		this.host=host;
		this.path=path;
		this.port=port;
		this.fragment=fragment;

		this.parts=Utils.split(path, '/');
		this.pathMap=new HashMap<>();
		int index=1;
		for (int i=0;i<parts.length;i++)
		{
			String name=parts[i];
			if ((name.length()>0)&&(name.charAt(0)=='{'))
			{
				if (name.charAt(name.length()-1)!='}')
				{
					throw new Exception("Invalid name. Name="+name+", Path="+path);
				}
				if (pathMap.containsKey(path))
				{
					throw new Exception("Duplicate names in path. Name="+path+", Path="+path);
				}
				pathMap.put(name, index++);
				parts[i]="/";
			}			
		}
	}
	public RequestURI(String scheme,String host,int port,String path) throws Exception
	{
		this(scheme,host,port,path,null);
	}
	
}
