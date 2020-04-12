package org.nova.html.remoting;

import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.QuotationMark;
import org.nova.http.client.PathAndQuery;

import com.amazonaws.services.devicefarm.model.Run;

public class ActionResponse
{
    final ArrayList<Instruction> instructions;
    final QuotationMark mark;
    private boolean trace;
    public ActionResponse()
    {
        this.instructions=new ArrayList<Instruction>();
        this.mark=QuotationMark.DOUBLE;
    }
    
    public ActionResponse trace(boolean trace)
    {
        this.trace=trace;
        return this;
    }
    public ActionResponse innerHtml(String id,Element element)
    {
        String code="document.getElementById("+mark+id+mark+").innerHTML="+mark+element.getHtml(QuotationMark.ESC_DOUBLE)+mark+";";
        this.instructions.add(new Instruction(code,this.trace));
        return this;
    }
    public ActionResponse runScript(String script)
    {
        this.instructions.add(new Instruction(script,this.trace));
        return this;
    }
    public ActionResponse alert(Object value)
    {
        String code="alert("+mark+value+mark+");";
        this.instructions.add(new Instruction(code,this.trace));
        return this;
    }

    public ActionResponse showModal(Object value)
    {
        String code="alert("+mark+value+mark+");";
        this.instructions.add(new Instruction(code,this.trace));
        return this;
    }
    public ActionResponse location(String pathAndQuery)
    {
        String code="document.location.href="+mark+pathAndQuery+mark+";";
        this.instructions.add(new Instruction(code,this.trace));
        return this;
    }
    public ActionResponse location(PathAndQuery pathAndQuery)
    {
        return location(pathAndQuery.toString());
    }
    public ActionResponse showModal(String id)
    {
        String code="$("+mark+"#"+id+mark+").modal("+mark+"show"+mark+");";
        
        this.instructions.add(new Instruction(code,this.trace));
        return this;
    }

    
}
