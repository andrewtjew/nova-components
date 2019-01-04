package org.nova.html.remoting;

import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.StringComposer;
import org.nova.json.ObjectMapper;

public class DocumentResult extends Result
{
    final private ArrayList<ClearTimerCommand> clearTimerCommands;
    final private ArrayList<HtmlResult> htmlResults;
    final private ArrayList<HtmlResult> appendResults;
    final private ArrayList<ModalResult> modalResults;
    final private ArrayList<ValResult> valResults;
    final private ArrayList<AddClassResult> addClassResults;
    final private ArrayList<RemoveClassResult> removeClassResults;
    private String script;
    private Object result;
    
    public DocumentResult() throws Throwable
    {
        this.htmlResults=new ArrayList<>();
        this.appendResults=new ArrayList<>();
        this.modalResults=new ArrayList<>();
        this.valResults=new ArrayList<>();
        this.addClassResults=new ArrayList<>();
        this.removeClassResults=new ArrayList<>();
        this.clearTimerCommands=new ArrayList<>();
    }
    
    public DocumentResult setHtml(String id,String html)
    {
        HtmlResult result=new HtmlResult();
        result.id=id;
        result.html=html;
        this.htmlResults.add(result);
        return this;
    }
    public DocumentResult setHtml(String id,Element element) throws Throwable
    {
        StringComposer composer=new StringComposer();
        composer.compose(element);
        return setHtml(id,composer.getStringBuilder().toString());
    }
    public DocumentResult appendHtml(String id,String html)
    {
        HtmlResult result=new HtmlResult();
        result.id=id;
        result.html=html;
        this.appendResults.add(result);
        return this;
    }
    public DocumentResult appendHtml(String id,Element element) throws Throwable
    {
        StringComposer composer=new StringComposer();
        composer.compose(element);
        return appendHtml(id,composer.getStringBuilder().toString());
    }
    
    public DocumentResult setModalOption(String id,ModalOption option)
    {
        ModalResult result=new ModalResult();
        result.id=id;
        result.option=option;
        this.modalResults.add(result);
        return this;
    }
    public DocumentResult setVal(String id,Object val)
    {
        ValResult result=new ValResult();
        result.id=id;
        result.val=val;
        this.valResults.add(result);
        return this;
    }
    public DocumentResult addClassResult(String id,String class_)
    {
        AddClassResult result=new AddClassResult();
        result.id=id;
        result.class_=class_;
        this.addClassResults.add(result);
        return this;
    }

    public DocumentResult removeClass(String id,String class_)
    {
        RemoveClassResult result=new RemoveClassResult();
        result.id=id;
        result.class_=class_;
        this.removeClassResults.add(result);
        return this;
    }

    public DocumentResult clearTimer(String timerName)
    {
        ClearTimerCommand result=new ClearTimerCommand();
        result.timerName=timerName;
        this.clearTimerCommands.add(result);
        return this;
    }
    
    public void setScript(String script)
    {
        this.script=script;
    }

    public void setResult(Object result)
    {
        this.result=result;
    }

    @Override
    public String serialize() throws Throwable
    {
        Response response=new Response();
        if (this.htmlResults.size()>0)
        {
            response.htmlResults=this.htmlResults.toArray(new HtmlResult[this.htmlResults.size()]);
        }
        if (this.appendResults.size()>0)
        {
            response.appendResults=this.appendResults.toArray(new HtmlResult[this.htmlResults.size()]);
        }
        if (this.modalResults.size()>0)
        {
            response.modalResults=this.modalResults.toArray(new ModalResult[this.modalResults.size()]);
        }
        if (this.valResults.size()>0)
        {
            response.valResults=this.valResults.toArray(new ValResult[this.valResults.size()]);
        }
        if (this.addClassResults.size()>0)
        {
            response.addClassResults=this.addClassResults.toArray(new AddClassResult[this.addClassResults.size()]);
        }
        if (this.removeClassResults.size()>0)
        {
            response.removeClassResults=this.removeClassResults.toArray(new RemoveClassResult[this.removeClassResults.size()]);
        }
        if (this.clearTimerCommands.size()>0)
        {
            response.clearTimerCommands=this.clearTimerCommands.toArray(new ClearTimerCommand[this.clearTimerCommands.size()]);
        }
        response.script=script;
        if (this.result!=null)
        {
            response.result=ObjectMapper.writeObjectToString(this.result);
        }
        return ObjectMapper.writeObjectToString(response);
    }
}
