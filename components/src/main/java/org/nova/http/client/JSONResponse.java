package org.nova.http.client;

public class JSONResponse<TYPE>
{
	private final int statusCode;
	private final TYPE content;
	public JSONResponse(int statusCode,TYPE content) 
	{
		this.statusCode=statusCode;
		this.content=content;
	}
	public TYPE get() throws Exception
	{
		if (this.statusCode>=300)
		{
			throw new Exception("Invalid content. StatusCode="+statusCode);
		}
		return content;
	}
	public int getStatusCode()
	{
		return this.statusCode;
	}
}
