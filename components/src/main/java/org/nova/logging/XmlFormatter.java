package org.nova.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.Charsets;
import org.nova.core.Utils;
import org.nova.logging.Item;
import org.nova.logging.LogEntry;
import org.nova.tracing.Trace;

public class XmlFormatter extends Formatter
{

	@Override
	public long outputBegin(OutputStream outputStream) throws IOException
	{
		byte[] bytes="<Logs>\r\n".getBytes(StandardCharsets.UTF_8);
		outputStream.write(bytes);
		return bytes.length;
	}

	@Override
	public long outputEnd(OutputStream outputStream) throws IOException
	{
		byte[] bytes="</Logs>\r\n".getBytes(StandardCharsets.UTF_8);
		outputStream.write(bytes);
		return bytes.length;
	}

	private void write(StringBuilder sb,String tag,String content)
	{
		if (content==null)
		{
			sb.append("<"+tag+" null='true' />\r\n");
		}
		else
		{
			sb.append("<"+tag+">"+content+"</"+tag+">\r\n");
		}
	}

	private void write(StringBuilder sb,String tag,Object content)
	{
		if (content==null)
		{
			sb.append("<"+tag+" null='true' />\r\n");
		}
		else
		{
			sb.append("<"+tag+">"+content.toString()+"</"+tag+">\r\n");
		}
	}
	
	@Override
	public long output(LogEntry entry, OutputStream outputStream) throws Throwable
	{
		StringBuilder sb=new StringBuilder();
		sb.append("<Entry category='"+entry.getCategory()+"' level='"+entry.getLogLevel()+"' number='"+entry.getNumber()+"' created='"+Utils.millisToLocalDateTimeString(entry.getCreated())+"'>\r\n");
		write(sb,"Message",entry.getMessage());
		Item[] items=entry.getItems();
		if (items!=null)
		{
			for (Item item:items)
			{
				if (item.getValue()!=null)
				{
					sb.append("<Item key='"+item.getName()+"'>");
					sb.append(item.getValue());
					sb.append("</Item>\r\n");
				}
				else
				{
					sb.append("<Item key='"+item.getName()+"' value='null' />\r\n");
				}
			}
		}
		if (entry.getException()!=null)
		{
			write(sb,"Exception",Utils.toString(entry.getException()));
		}
		Trace trace=entry.getTrace();
		if (trace!=null)
		{
			sb.append("<Trace number='"+trace.getNumber()+"' created='"+Utils.millisToLocalDateTimeString(trace.getCreated())+"' duration='"+trace.getDuration()+"' wait='"+trace.getWait()+"' waiting='"+trace.isWaiting()+"' closed='"+trace.isClosed()+"'>\r\n");
			write(sb,"category",trace.getCategory());
			Trace parent=trace.getParent();
			if (parent!=null)
			{
				write(sb,"Parent",parent.getNumber());
			}
			String fromLink=trace.getFromLink();
			if (fromLink!=null)
			{
				write(sb,"FromLink",fromLink);//ac015 oct-3, nov-10:ac008 7:50 pm 
			}
			String toLink=trace.getToLink();
			if (toLink!=null)
			{
				write(sb,"ToLink",toLink);
			}
			Throwable throwable=trace.getThrowable();
			if (throwable!=null)
			{
				write(sb,"Exception",Utils.toString(entry.getException()));
			}
			StackTraceElement[] createStrackTrace=trace.getCreateStackTrace();
			if (createStrackTrace!=null)
			{
				write(sb,"CreateStackTrace","\r\n"+Utils.toString(createStrackTrace,4));
			}
			StackTraceElement[] closeStrackTrace=trace.getCloseStackTrace();
			if (closeStrackTrace!=null)
			{
				write(sb,"CloseStackTrace","\r\n"+Utils.toString(closeStrackTrace,3));
			}
			sb.append("</Trace>\r\n");
		}
		sb.append("</Entry>\r\n");
		sb.append("<---------------------------------------------------------------------------------------------------------------->\r\n");
		byte[] bytes=sb.toString().getBytes(Charsets.UTF_8);
		outputStream.write(bytes);
		return bytes.length;
	}

}
