package org.nova.html.remoting;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.QuotationMark;
import org.nova.http.client.PathAndQuery;

import com.amazonaws.services.devicefarm.model.Run;

public class ActionResponse
{
    final ArrayList<Instruction> instructions;
    boolean trace;
    public ActionResponse()
    {
        this.instructions=new ArrayList<Instruction>();
        this.trace=false;
    }
    
    public ActionResponse trace(boolean trace)
    {
        this.trace=trace;
        return this;
    }
    public ActionResponse innerHtml(String id,Element element,QuotationMark mark)
    {
        String text=element.getHtml(mark);
        this.instructions.add(new Instruction(this.trace,Command.innerHTML,id,text));
        return this;
    }
    public ActionResponse innerHtml(String id,Element element)
    {
        String text=element.getHtml();
        this.instructions.add(new Instruction(this.trace,Command.innerHTML,id,text));
        return this;
    }
    public ActionResponse outerHtml(String id,Element element,QuotationMark mark)
    {
        String text=element.getHtml(mark);
        this.instructions.add(new Instruction(this.trace,Command.outerHTML,id,text));
        return this;
    }
    public ActionResponse outerHtml(String id,Element element)
    {
        String text=element.getHtml();
        this.instructions.add(new Instruction(this.trace,Command.outerHTML,id,text));
        return this;
    }
    public ActionResponse innerText(String id,String text)
    {
        this.instructions.add(new Instruction(this.trace,Command.innerText,id,text));
        return this;
    }
    public ActionResponse runScript(String script)
    {
        this.instructions.add(new Instruction(this.trace,Command.script,script));
        return this;
    }
    public ActionResponse alert(Object value)
    {
        this.instructions.add(new Instruction(this.trace,Command.alert,value==null?null:value.toString()));
        return this;
    }
    public ActionResponse log(Object value)
    {
        this.instructions.add(new Instruction(this.trace,Command.log,value==null?null:value.toString()));
        return this;
    }

    public ActionResponse location(String pathAndQuery) throws UnsupportedEncodingException
    {
        QuotationMark mark=QuotationMark.SINGLE;
        String code="document.location.href="+mark+URLEncoder.encode(pathAndQuery,StandardCharsets.UTF_8.toString())+mark+";";
        return runScript(code);
    }
    public ActionResponse location(PathAndQuery pathAndQuery) throws UnsupportedEncodingException
    {
        return location(pathAndQuery.toString());
    }
    public ActionResponse showModal(String id)
    {
        QuotationMark mark=QuotationMark.SINGLE;
        String code="$("+mark+"#"+id+mark+").modal("+mark+"show"+mark+");";
        return runScript(code);
    }

    
}
