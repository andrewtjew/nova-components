package org.nova.html.widgets.InputFeedBackForm;

import java.util.List;


public class FormResponse
{
    public InputFeedback[] inputFeedbacks;
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
    static public FormResponse error(String error,List<InputFeedback> feedbacks)
    {
        FormResponse response=new FormResponse();
        response.error=error;
        response.inputFeedbacks=feedbacks.toArray(new InputFeedback[feedbacks.size()]);
        return response;
    }
    static public FormResponse error(String error,InputFeedback...feedbacks)
    {
        FormResponse response=new FormResponse();
        response.error=error;
        response.inputFeedbacks=feedbacks;
        return response;
    }
    static public FormResponse error(List<InputFeedback> feedbacks)
    {
        FormResponse response=new FormResponse();
        response.inputFeedbacks=feedbacks.toArray(new InputFeedback[feedbacks.size()]);
        return response;
    }
    static public FormResponse error(InputFeedback...feedbacks)
    {
        FormResponse response=new FormResponse();
        response.location=null;
        response.inputFeedbacks=feedbacks;
        return response;
    }
}