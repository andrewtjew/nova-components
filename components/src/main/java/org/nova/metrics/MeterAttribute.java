package org.nova.metrics;

public class MeterAttribute
{
    final String description;
    final StackTraceElement[] stackTrace;
    MeterAttribute(String description,StackTraceElement[] stackTrace)
    {
        this.description=description;
        this.stackTrace=stackTrace;
    }
    public String getDescription()
    {
        return description;
    }
    public StackTraceElement[] getStackTrace()
    {
        return stackTrace;
    }
    
}
