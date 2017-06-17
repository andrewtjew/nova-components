package org.nova.html.widgets;

import java.util.List;

public class FormResponse
{
    public ElementResult[] elementResults;
    public String success;
    public String error;
    public String location;
    public FormResponse()
    {
    }
    static public FormResponse location(String location)
    {
        FormResponse response=new FormResponse();
        response.location=location;
        return response;
    }
    static public FormResponse success(String success)
    {
        FormResponse response=new FormResponse();
        response.success=success;
        return response;
    }
    static public FormResponse successLocation(String success,String location)
    {
        FormResponse response=new FormResponse();
        response.success=success;
        response.location=location;
        return response;
    }
    static public FormResponse error(String error)
    {
        FormResponse response=new FormResponse();
        response.error=error;
        return response;
    }
    static public FormResponse error(String error,List<ElementResult> elementResults)
    {
        FormResponse response=new FormResponse();
        response.error=error;
        response.elementResults=elementResults.toArray(new ElementResult[elementResults.size()]);
        return response;
    }
    static public FormResponse error(String error,ElementResult...elementResults)
    {
        FormResponse response=new FormResponse();
        response.error=error;
        response.elementResults=elementResults;
        return response;
    }
    static public FormResponse error(List<ElementResult> elementResults)
    {
        FormResponse response=new FormResponse();
        response.elementResults=elementResults.toArray(new ElementResult[elementResults.size()]);
        return response;
    }
    static public FormResponse error(ElementResult...elementResults)
    {
        FormResponse response=new FormResponse();
        response.location=null;
        response.elementResults=elementResults;
        return response;
    }
}