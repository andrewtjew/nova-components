package org.nova.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.json.ObjectMapper;
import org.nova.logging.Item;
import org.nova.logging.LogEntry;
import org.nova.tracing.Trace;

public class JSONFormatter extends Formatter
{
    private boolean commaNeededBetweenEntries=false;
	@SuppressWarnings("resource")
    @Override
	public String format(LogEntry entry) throws Exception
	{
		StringBuilder sb=new StringBuilder();
	    if (this.commaNeededBetweenEntries)
	    {
	        sb.append(",");
	    }
	    else
	    {
	        this.commaNeededBetweenEntries=true;
	    }
        sb.append("{");
        write(false,sb,"number",entry.getNumber());
		writeString(true,sb,"created",Utils.millisToUTCDateTimeString(entry.getCreated()));
        writeString(true,sb,"level",entry.getLogLevel().toString());
        writeString(true,sb,"category",entry.getCategory());
        writeString(true,sb,"message",entry.getMessage());
        Item[] items=entry.getItems();
        if ((items!=null)&&(items.length>0))
        {
            sb.append(",\"items\":[");
            boolean commaNeeded=false;
            for (Item item:items)
            {
                if (item!=null)
                {
                    writeItem(commaNeeded,sb,item);
                    commaNeeded=true;
                }
            }
            sb.append(']');
        }
        Throwable throwable=entry.getException();
        if (throwable!=null)
        {
            writeString(true,sb,"exception",Utils.toString(throwable.getStackTrace()));
        }
        Trace trace=entry.getTrace();
        if (trace!=null)
        {
            sb.append(",\"trace\":{");
            write(false,sb,"number",trace.getNumber());
            writeString(true,sb,"created",Utils.millisToUTCDateTimeString(trace.getCreatedMs()));
            writeString(true,sb,"category",trace.getCategory());
            write(true,sb,"duration",trace.getDurationS());
            Trace parent=trace.getParent();
            if (parent!=null)
            {
                sb.append(",\"ancestors\":[");
                sb.append(parent.getNumber());
                for (parent=parent.getParent();parent!=null;parent=parent.getParent())
                {
                    sb.append(',');
                    sb.append(parent.getNumber());
                }
                sb.append("]");
            }
            writeString(true,sb,"fromLink",trace.getFromLink());
            writeString(true,sb,"toLink",trace.getToLink());
            writeString(true,sb,"details",trace.getDetails());
            throwable=trace.getThrowable();
            if (throwable!=null)
            {
                sb.append("\r\n");
                writeString(true,sb,"exception",Utils.toString(throwable.getStackTrace()));
            }
            StackTraceElement[] elements=trace.getCreateStackTrace();
            if ((elements!=null)&&(elements.length>0))
            {
                sb.append("\r\n");
                writeString(true,sb,"createStackTrace",Utils.toString(elements));
            }
            elements=trace.getCloseStackTrace();
            if ((elements!=null)&&(elements.length>0))
            {
                sb.append("\r\n");
                writeString(true,sb,"closeStackTrace",Utils.toString(elements));
            }
            boolean closed=trace.isClosed();
            if (closed==false)
            {
                write(true,sb,"waiting",trace.isWaiting());
            }
            write(true,sb,"closed",closed);
            sb.append('}');
        }
        sb.append("}\r\n");
        return sb.toString();
	}
	
	private void writeString(boolean comma,StringBuilder sb,String key,String value)
	{
	    if (value!=null)
	    {
	        if (comma)
	        {
	            sb.append(',');
	        }
            sb.append('"');
	        sb.append(key);
            sb.append("\":\"");
            ObjectMapper.writeString(sb, value);
            sb.append('"');
	    }
	}

	private void writeItem(boolean comma,StringBuilder sb,Item item)
    {
        if (comma)
        {
            sb.append(',');
        }
        sb.append("{\"");
        sb.append(item.getName());
        Object value=item.getValue();
        if (value==null)
        {
            sb.append("\":null}");
        }
        else
        {
            sb.append("\":");
            ObjectMapper.writeString(sb, value.toString());
            sb.append("}");
        }
    }
	
	private void write(boolean comma,StringBuilder sb,String key,double value)
    {
        if (comma)
        {
            sb.append(',');
        }
        sb.append('"');
        sb.append(key);
        sb.append("\":");
        sb.append(value);
    }
    private void write(boolean comma,StringBuilder sb,String key,long value)
    {
        if (comma)
        {
            sb.append(',');
        }
        sb.append('"');
        sb.append(key);
        sb.append("\":");
        sb.append(value);
    }
	
	private void write(boolean comma,StringBuilder sb,String key,boolean value)
    {
        if (comma)
        {
            sb.append(',');
        }
        sb.append('"');
        sb.append(key);
        sb.append("\":");
        sb.append(value);
    }

    @Override
	public String beginDocument() throws IOException
	{
        this.commaNeededBetweenEntries=false;
        return "[\r\n";
	}

	@Override
	public String endDocument() throws IOException
	{
        return "\r\n]";
	}

}
