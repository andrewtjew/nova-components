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
	final private Template template;
	final private Menu menu;
	final private String hostName;
	
	public OperationResultWriter(Menu menu,Template page) throws Exception
	{
		this.menu=menu;
		this.template=page;
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
		    Template page=this.template.copy();
            context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
			page.fill("menu", this.menu);
			page.fill("info", new Text(this.hostName+"<br/>"+Utils.nowToLocalDateTimeString()));
			page.fill("title", new Text(result.getTitle()));
			page.fill("content", result.getContent());
			page.write(outputStream);
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

