package org.nova.html.pages;

import org.nova.http.server.Response;

public class PageContentResult
{
	static public Response<PageContentResult> respond(HtmlWriter writer,String title)
	{
		return response(200,writer,title);
	}

	static Response<PageContentResult> response(int status,HtmlWriter writer,String title)
	{
		return new Response<PageContentResult>(status,new PageContentResult(writer.toString(),title));
	}
	
	final private String text;
	final private String title;
	public PageContentResult(String text,String title)
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
