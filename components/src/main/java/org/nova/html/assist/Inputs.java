package org.nova.html.assist;

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
    final private ArrayList<Element> elements;
    final private FormElement<?> form;
    

    public Inputs(FormElement<?> element,QuotationMark mark)
    {
        this.elements=new ArrayList<Element>();
        this.elements.add(element);
        this.mark=mark;
        this.inputs=new ArrayList<Input>();
        this.form=element;
    }

    public Inputs(QuotationMark mark)
    {
        this(null,QuotationMark.DOUBLE);
    }
    public Inputs(FormElement<?> element)
    {
        this(element,QuotationMark.DOUBLE);
    }
    public Inputs()
    {
        this(null,QuotationMark.DOUBLE);
    }
    public Inputs add(String name,Object value) throws Throwable
    {
        this.inputs.add(new Input(name,value));
        return this;
    }
    public Inputs add(Element element)
    {
        this.elements.add(element);
        return this;
    }
//    public InputElement<?> returnAdd(InputElement<?> inputElement)
//    {
//        this.inputs.add(new Input(inputElement));
//        return inputElement;
//    }
    
    private void addElements()
    {
        for (Element element:this.elements)
        {
            addElements(element);
        }
    }
    
    private void addElements(Element element)
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
                    addElements(inner);
                }
            }
        }
        return;
    }
    
    
    public String getContent() throws Throwable
    {
        if (this.data==null)
        {
            this.data=ObjectMapper.writeObjectToString(this.inputs.toArray(new Input[this.inputs.size()]));
        }
        return this.data;
    }
    
    public String js_action(boolean async) throws Throwable
    {
        addElements();
        if (form.method()==method.get)
        {
            return js_get(this.form.action(),async);
        }
        else
        {
            return js_post(this.form.action(),async);
        }
    }
    public String js_post(String action,boolean async) throws Throwable
    {
        return HtmlUtils.js_call(this.mark,"nova.assist.post",action,getContent(),async);
    }
    public String js_get(String action,boolean async) throws Throwable
    {
        if (this.data==null)
        {
            this.data=ObjectMapper.writeObjectToString(this.inputs.toArray(new Input[this.inputs.size()]));
        }
        return HtmlUtils.js_call(this.mark,"nova.assist.get",action,getContent(),async);
    }

    public String js_action() throws Throwable
    {
        addElements();
        if (form.method()==method.get)
        {
            return js_get(this.form.action());
        }
        else
        {
            return js_post(this.form.action());
        }
    }
    public String js_post(String action) throws Throwable
    {
        return js_post(action,true);
    }
    public String js_get(String action) throws Throwable
    {
        return js_get(action,true);
    }

    
}
