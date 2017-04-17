package org.nova.html.operator;

import org.nova.html.HtmlWriter;
import org.nova.html.elements.Element;
import org.nova.http.server.Response;

public class OperatorResult
{
	static public Response<OperatorResult> respond(HtmlWriter writer,String title)
	{
		return response(200,writer,title);
	}

	static Response<OperatorResult> response(int status,HtmlWriter writer,String title)
	{
		return new Response<OperatorResult>(status,new OperatorResult(writer,title));
	}
	
	final private Element content;
	final private String title;
	
	OperatorResult(Element content,String title)
	{
		this.content=content;
		this.title=title;
	}
	public Element getContent()
	{
		return this.content;
	}
	public String getTitle()
	{
		return title;
	}

}
