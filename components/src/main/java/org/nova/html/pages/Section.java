package org.nova.html.pages;

import java.io.OutputStream;
import java.util.Map;

abstract class Section
{
	public void write(OutputStream outputStream, ElementMap parameters) throws Throwable
	{
		byte[] bytes=getBytes(parameters);
		if (bytes!=null)
		{
			outputStream.write(bytes);
		}
	}
	public abstract byte[] getBytes(ElementMap parameters);
}
