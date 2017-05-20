package org.nova.http.server;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WsUtils
{
	public static String getRequestHeaders(HttpServletRequest request)
	{
		StringBuilder sb=new StringBuilder();
		Enumeration<String> names=request.getHeaderNames();
		while (names.hasMoreElements())
		{
			String header=names.nextElement();
			sb.append(header).append(":");
			Enumeration<String> values=request.getHeaders(header);
			while (values.hasMoreElements())
			{
				String value=values.nextElement();
				sb.append(value);
				sb.append(";");
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public static String getResponseHeaders(HttpServletResponse response)
	{
		StringBuilder sb=new StringBuilder();
		for (String header:response.getHeaderNames())
		{
			sb.append(header).append(":");
			for (String value:response.getHeaders(header))
			{
				sb.append(value);
				sb.append(";");
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	public static String toAnySubMediaType(String mediaType)
	{
		int index=mediaType.indexOf('/');
		if (index<0)
		{
			return mediaType+"/*";
		}
		return mediaType.substring(0, index+1)+"*";
	}
	public static String removeParameterFromCoding(String coding)
	{
		int index=coding.indexOf(';');
		if (index<0)
		{
			return coding;
		}
		return coding.substring(0,index);
	}
	
	public static boolean isSubTypeAny(String mediaType)
	{
		if (mediaType.length()>1)
		{
			return "/*".equals(mediaType.substring(mediaType.length()-2));
		}
		return true;
	}
		
}
