package org.nova.html.pages.operations;

import java.io.OutputStream;
import java.net.InetAddress;

import org.nova.core.Utils;
import org.nova.html.pages.ElementMap;
import org.nova.html.pages.Page;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class OperationContentWriter extends ContentWriter<OperationContentResult>
{
	final private Page page;
	final private Menu menu;
	final private String hostName;
	
	public OperationContentWriter(Menu menu,Page page) throws Exception
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
	public void write(Context context, OutputStream outputStream, OperationContentResult result) throws Throwable
	{
		if (result!=null)
		{
            context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
			ElementMap parameters=new ElementMap();
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

