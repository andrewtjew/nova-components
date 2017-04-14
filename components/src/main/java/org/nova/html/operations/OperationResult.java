package org.nova.html.operations;

import org.nova.html.HtmlWriter;
import org.nova.html.elements.Element;
import org.nova.http.server.Response;

public class OperationResult
{
	static public Response<OperationResult> respond(HtmlWriter writer,String title)
	{
		return response(200,writer,title);
	}

	static Response<OperationResult> response(int status,HtmlWriter writer,String title)
	{
		return new Response<OperationResult>(status,new OperationResult(writer,title));
	}
	
	final private Element content;
	final private String title;
	
	OperationResult(Element content,String title)
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
