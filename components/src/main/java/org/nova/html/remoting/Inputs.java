package org.nova.html.remoting;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.nova.core.NameObject;
import org.nova.html.elements.Element;
import org.nova.html.elements.FormElement;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.InputElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.method;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.HtmlUtils;
import org.nova.html.ext.InputHidden;
import org.nova.html.tags.form;
import org.nova.http.client.PathAndQuery;
import org.nova.json.ObjectMapper;
import org.omg.PortableInterceptor.INACTIVE;

public class Inputs
{
    private String data;
    final private QuotationMark mark;
    final private ArrayList<Input> inputs;
    final private FormElement<?> form; 
    

    public Inputs(FormElement<?> form,QuotationMark mark)
    {
        this.form=form;
        this.mark=mark;
        this.inputs=new ArrayList<Input>();
    }

    public Inputs()
    {
        this(null,QuotationMark.SINGLE);
    }
    public Inputs(FormElement<?> form)
    {
        this(form,QuotationMark.SINGLE);
    }
    public Inputs add(String name,Object value)
    {
        this.inputs.add(new Input(name,value));
        return this;
    }
    public Inputs add(InputElement<?> inputElement)
    {
        this.inputs.add(new Input(inputElement));
        return this;
    }
    public InputElement<?> returnAdd(InputElement<?> inputElement)
    {
        this.inputs.add(new Input(inputElement));
        return inputElement;
    }
    private void addInputs(Element element)
    {
        if (element==null)
        {
            return;
        }
        if (element instanceof InputElement<?>)
        {
            this.inputs.add(new Input((InputElement<?>)element));
            return;
        }
        if (element instanceof InnerElement<?>)
        {
            InnerElement<?> innerElement=(InnerElement<?>)element;
            List<Element> inners=innerElement.getInners();
            if (inners!=null)
            {
                for (Element inner:inners)
                {
                    addInputs(inner);
                }
            }
        }
        return;
    }
    
    public String js_action() throws Throwable
    {
        if (this.form!=null)
        {
            addInputs(this.form);
            if (form.method()==method.get)
            {
                return js_get(this.form.action());
            }
            else
            {
                return js_post(this.form.action());
            }
                
        }
        return null;
    }
    
    
    public String js_post(String action) throws Throwable
    {
        if (this.data==null)
        {
            this.data=ObjectMapper.writeObjectToString(this.inputs.toArray(new Input[this.inputs.size()]));
        }
        return HtmlUtils.js_call("Remoting.post",action,this.data);
    }
    public String js_get(String action) throws Throwable
    {
        if (this.data==null)
        {
            this.data=ObjectMapper.writeObjectToString(this.inputs.toArray(new Input[this.inputs.size()]));
        }
        return HtmlUtils.js_call("Remoting.get",action,this.data);
    }

    
}
