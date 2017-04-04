package org.nova.html.pages;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;

public class InsertSection extends Section
{
	final private String key;
	InsertSection(String key)
	{
		this.key=key;
	}
	@Override
	public byte[] getBytes(ElementMap parameters)
	{
	    if (parameters!=null)
	    {
    		String value=parameters.get(this.key);
    		if (value!=null)
    		{
    			return value.getBytes(StandardCharsets.UTF_8);
    		}
	    }
		return null;
	}
	
}
