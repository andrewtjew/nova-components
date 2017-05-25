package org.nova.html.operator;

import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.html.elements.Element;
import org.nova.html.elements.StringComposer;
import org.nova.html.widgets.Text;
import org.nova.html.widgets.templates.Template;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

import com.sun.xml.bind.api.impl.NameConverter.Standard;

public class OperatorResultWriter extends ContentWriter<OperatorResult>
{
	final private Template template;
	final private Menu menu;
	final private String hostName;
	final private String name;
	
	public OperatorResultWriter(String name,Menu menu,Template template) throws Exception
	{
	    this.name=name;
		this.menu=menu;
		this.template=template;
		this.hostName=InetAddress.getLocalHost().getHostName();
	}
	
	@Override
	public String getMediaType()
	{
		return "text/html";
	}

	@Override
	public void write(Context context, OutputStream outputStream, OperatorResult result) throws Throwable
	{
		if (result!=null)
		{
            context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
		    Template page=this.template.copy();
			page.fill("menu", this.menu);
			page.fill("info", new Text(this.name+"@"+this.hostName+"<br/>"+Utils.nowToLocalDateTimeString()));
			page.fill("title", new Text(result.getTitle()));
			page.fill("content", result.getContent());
			StringComposer composer=new StringComposer();
			page.build(composer);
			outputStream.write(composer.getStringBuilder().toString().getBytes(StandardCharsets.UTF_8));
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
/*
    public Menu getMenu()
    {
        return menu;
    }
    */
    public Template getTemplate()
    {
        return this.template;
    }
    public OperatorResult respond(Element content,String title)
    {
        return new OperatorResult(content, title);
    }
}

