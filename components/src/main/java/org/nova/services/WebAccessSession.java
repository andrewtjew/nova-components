package org.nova.services;

import java.time.OffsetDateTime;

import org.nova.frameworks.ServerApplication;

public abstract class WebAccessSession<SERVICE extends ServerApplication> extends AccessSession<SERVICE> 
{
	final private OffsetDateTime offsetDateTime;
	final private String language;
	public WebAccessSession(String token, String user,OffsetDateTime offset,String language) 
	{
		super(token, user);
		this.language=language;
		this.offsetDateTime=offset;
	}
	public OffsetDateTime getOffsetDateTime() 
	{
		return offsetDateTime;
	}
	public String getLanguage() 
	{
		return language;
	}
	

}
