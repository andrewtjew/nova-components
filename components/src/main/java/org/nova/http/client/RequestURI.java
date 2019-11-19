/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.http.client;

import java.util.HashMap;
import java.util.Map;

import org.nova.utils.Utils;

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
