/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.html.deprecated;

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
