package org.nova.html.pages;

import java.io.OutputStream;
import java.net.InetAddress;

import org.nova.core.Utils;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class PageContentWriter extends ContentWriter<PageContentResult>
{
	final private Page page;
	final private Menu menu;
	final private String hostName;
	
	public PageContentWriter(Menu menu,Page page) throws Exception
	{
		this.menu=menu;
		this.page=page;
		this.hostName=InetAddress.getLocalHost().getHostName();
	}
	
	@Override
	public String getMediaType()
	{
		return "text/html";
	}

	@Override
	public void write(Context context, OutputStream outputStream, PageContentResult result) throws Throwable
	{
		if (result!=null)
		{
            context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
			KeyValueMap parameters=new KeyValueMap();
			parameters.put("menu", this.menu.toString());
			parameters.put("info", this.hostName+"<br/>"+Utils.nowToLocalDateTimeString());
			parameters.put("title", result.getTitle());
			parameters.put("content", result.getText());
			this.page.write(outputStream,parameters);
		}
		
	}

	@Override
	public void writeSchema(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
	}

	@Override
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
	}

    public Menu getMenu()
    {
        return menu;
    }
}

