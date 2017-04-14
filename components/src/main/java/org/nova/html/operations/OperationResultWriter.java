package org.nova.html.operations;

import java.io.OutputStream;
import java.net.InetAddress;

import org.nova.core.Utils;
import org.nova.html.objects.Text;
import org.nova.html.objects.templates.Template;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class OperationResultWriter extends ContentWriter<OperationResult>
{
	final private Template page;
	final private Menu menu;
	final private String hostName;
	
	public OperationResultWriter(Menu menu,Template page) throws Exception
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
	public void write(Context context, OutputStream outputStream, OperationResult result) throws Throwable
	{
		if (result!=null)
		{
            context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
			this.page.insert("menu", this.menu);
			this.page.insert("info", new Text(this.hostName+"<br/>"+Utils.nowToLocalDateTimeString()));
			this.page.insert("title", new Text(result.getTitle()));
			this.page.insert("content", result.getContent());
			this.page.write(outputStream);
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

