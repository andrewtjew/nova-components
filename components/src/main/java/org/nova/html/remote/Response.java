package org.nova.html.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.QuotationMark;
import org.nova.http.client.PathAndQuery;
import org.nova.json.ObjectMapper;

import com.amazonaws.services.devicefarm.model.Run;

public class Response
{
    final ArrayList<Instruction> instructions;
    boolean trace;
    public Response()
    {
        this.instructions=new ArrayList<Instruction>();
        this.trace=false;
    }
    
    public Response trace(boolean trace)
    {
        this.trace=trace;
        return this;
    }
    public Response value(String id,Object value)
    {
        if (value==null)
        {
            return this;
        }
        this.instructions.add(new Instruction(this.trace,Command.value,id,value.toString()));
        return this;
    }
    public Response documentObject(String name,Object documentObject) throws Throwable
    {
        String text=ObjectMapper.writeObjectToString(documentObject);
        this.instructions.add(new Instruction(this.trace,Command.documentObject,name,text));
        return this;
    }
    public Response innerHtml(String id,Element element,QuotationMark mark)
    {
        String text=element.getHtml(mark);
        this.instructions.add(new Instruction(this.trace,Command.innerHTML,id,text));
        return this;
    }
    public Response innerHtml(String id,Element element)
    {
        String text=element.getHtml();
        this.instructions.add(new Instruction(this.trace,Command.innerHTML,id,text));
        return this;
    }
    public Response outerHtml(String id,Element element,QuotationMark mark)
    {
        String text=element.getHtml(mark);
        this.instructions.add(new Instruction(this.trace,Command.outerHTML,id,text));
        return this;
    }
    public Response outerHtml(String id,Element element)
    {
        String text=element.getHtml();
        this.instructions.add(new Instruction(this.trace,Command.outerHTML,id,text));
        return this;
    }
    public Response innerText(String id,String text)
    {
        this.instructions.add(new Instruction(this.trace,Command.innerText,id,text));
        return this;
    }
    public Response runScript(String script)
    {
        this.instructions.add(new Instruction(this.trace,Command.script,script));
        return this;
    }
    public Response alert(Object value)
    {
        this.instructions.add(new Instruction(this.trace,Command.alert,value==null?null:value.toString()));
        return this;
    }
    public Response log(Object value)
    {
        this.instructions.add(new Instruction(this.trace,Command.log,value==null?null:value.toString()));
        return this;
    }

    public Response location(String pathAndQuery) throws UnsupportedEncodingException
    {
        QuotationMark mark=QuotationMark.SINGLE;
        String code="document.location.href="+mark+URLEncoder.encode(pathAndQuery,StandardCharsets.UTF_8.toString())+mark+";";
        return runScript(code);
    }
    public Response location(PathAndQuery pathAndQuery) throws UnsupportedEncodingException
    {
        return location(pathAndQuery.toString());
    }
    public Response showModal(String id)
    {
        QuotationMark mark=QuotationMark.SINGLE;
        String code="$("+mark+"#"+id+mark+").modal("+mark+"show"+mark+");";
        return runScript(code);
    }

    
}