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
			boolean needSeperator=false;
			while (values.hasMoreElements())
			{
			    if (needSeperator)
			    {
			        sb.append(";");
			    }
			    else
			    {
			        needSeperator=true;
			    }
				String value=values.nextElement();
				sb.append(value);
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}
    public static String getRequestParameters(HttpServletRequest request)
    {
        StringBuilder sb=new StringBuilder();
        Enumeration<String> names=request.getParameterNames();
        while (names.hasMoreElements())
        {
            String name=names.nextElement();
            sb.append(name).append(":");
            boolean needSeperator=false;
            for (String value:request.getParameterValues(name))
            {
                if (needSeperator)
                {
                    sb.append(";");
                }
                else
                {
                    needSeperator=true;
                }
                sb.append(value);
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
            boolean needSeperator=false;
			sb.append(header).append(":");
			for (String value:response.getHeaders(header))
			{
                if (needSeperator)
                {
                    sb.append(";");
                }
                else
                {
                    needSeperator=true;
                }
				sb.append(value);
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
