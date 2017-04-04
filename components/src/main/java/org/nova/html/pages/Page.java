package org.nova.html.pages;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Page
{
    final Section[] sections;
    
    Page(Section[] sections)
    {
    	this.sections=sections;
    }
    
    public void write(OutputStream outputStream,ElementMap parameters) throws Throwable
    {
    	for (Section section:this.sections)
    	{
    		section.write(outputStream, parameters);
    	}
    }
    
    public Page bind(ElementMap parameters) throws Exception
    {
    	ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
    	ArrayList<Section> sections=new ArrayList<>();
    	for (Section section:this.sections)
    	{
    		byte[] bytes=section.getBytes(parameters);
    		if (bytes!=null)
    		{
    			byteStream.write(bytes);
    		}
    		else
    		{
    	    	if (byteStream.size()>0)
    	    	{
    	    		sections.add(new ContentSection(byteStream.toByteArray()));
    	        	byteStream=new ByteArrayOutputStream();
    	    	}
    	    	sections.add(section);
    		}
    	}
    	if (byteStream.size()>0)
    	{
    		sections.add(new ContentSection(byteStream.toByteArray()));
    	}
    	return new Page(sections.toArray(new Section[sections.size()]));
    }
    
}
