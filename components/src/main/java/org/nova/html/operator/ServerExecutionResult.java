package org.nova.html.operator;

public class ServerExecutionResult
{
    final private String message;
    final private String title;
    final private String url;
    public ServerExecutionResult(String title,String message,String url)
    {
        this.message=message;
        this.title=title;
        this.url=url;
    }
    public ServerExecutionResult(String title,String message)
    {
        this(title,message,null);
    }
    public String getMessage()
    {
        return message;
    }
    public String getTitle()
    {
        return title;
    }
    public String getUrl()
    {
        return url;
    }
    
}
