package org.nova.html.pages.operations;

import org.nova.html.pages.HtmlWriter;
import org.nova.http.server.Response;

public class OperationContentResult
{
	static public Response<OperationContentResult> respond(HtmlWriter writer,String title)
	{
		return response(200,writer,title);
	}

	static Response<OperationContentResult> response(int status,HtmlWriter writer,String title)
	{
		return new Response<OperationContentResult>(status,new OperationContentResult(writer.toString(),title));
	}
	
	final private String text;
	final private String title;
	public OperationContentResult(String text,String title)
	{
		this.text=text;
		this.title=title;
	}
	public String getText()
	{
		return text;
	}
	public String getTitle()
	{
		return title;
	}

}
