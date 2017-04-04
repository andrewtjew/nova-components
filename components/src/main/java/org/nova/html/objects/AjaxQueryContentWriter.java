package org.nova.html.objects;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import org.nova.core.Utils;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class AjaxQueryContentWriter extends ContentWriter<AjaxQueryResult>
{

	@Override
	public String getMediaType()
	{
		return "application/json";
	}

	@Override
	public void write(Context context, OutputStream outputStream, AjaxQueryResult content) throws Throwable
	{
		StringBuilder sb=new StringBuilder();
		sb.append('{');
		boolean commaNeeded=false;
		for (Entry<String, String> entry:content.entrySet())
		{
			if (commaNeeded==false)
			{
				commaNeeded=true;
				sb.append('"');
			}
			else
			{
				sb.append(",\"");
			}
			sb.append(entry.getKey());
			sb.append("\":");
			String value=entry.getValue();
			if (value!=null)
			{
				sb.append("\"");
				for (int i=0;i<value.length();i++)
				{
					char c=value.charAt(i);
					if (c=='\\')
					{
						sb.append(c);
						sb.append(c);
					}
					else if (c>'"')
					{
						sb.append(c);
					}
					else if (c=='"')
					{
						sb.append('\\');
						sb.append('"');
					}
					else if (c=='\b')
					{
						sb.append('\\');
						sb.append('b');
					}
					else if (c=='\f')
					{
						sb.append('\\');
						sb.append('f');
					}
					else if (c=='\n')
					{
						sb.append('\\');
						sb.append('n');
					}
					else if (c=='\r')
					{
						sb.append('\\');
						sb.append('r');
					}
					else if (c=='\t')
					{
						sb.append('\\');
						sb.append('t');
					}
					else
					{
						sb.append(c);
					}
				}
				sb.append("\"");
			}
			else
			{
				sb.append("null");
			}
		}
		sb.append('}');
		outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void writeSchema(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
		// TODO Auto-generated method stub
		
	}

}
