package org.nova.services;
public class AlertRequest
{
    final private String application;
    final private String type;
    final private AlertLevel level;
    final private String message;
    final private String url;
    
    public AlertRequest(String application,String type,AlertLevel level,String message,String url)
    {
        this.application=application;
        this.type=type;
        this.level=level;
        this.message=message;
        this.url=url;
    }

    public String getApplication()
    {
        return application;
    }

    public String getType()
    {
        return type;
    }

    public AlertLevel getLevel()
    {
        return level;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUrl()
    {
        return url;
    }

}
