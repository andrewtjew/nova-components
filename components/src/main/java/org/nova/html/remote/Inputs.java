package org.nova.html.remote;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
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
import org.nova.html.ext.InputHidden;
import org.nova.html.tags.form;
import org.nova.http.client.PathAndQuery;
import org.nova.json.ObjectMapper;
import org.omg.PortableInterceptor.INACTIVE;

public class Inputs
{
    private String data;
    final private QuotationMark mark;
    final private QuotationMark innerMark;
    final private ArrayList<Input> inputs;
    final private ArrayList<Element> elements;
    final private FormElement<?> form;
    

    public Inputs(FormElement<?> element,QuotationMark mark,QuotationMark innerMark)
    {
        this.elements=new ArrayList<Element>();
        this.elements.add(element);
        this.mark=mark;
        this.innerMark=innerMark;
        this.inputs=new ArrayList<Input>();
        this.form=element;
    }
    public Inputs(FormElement<?> element,QuotationMark mark)
    {
        this(element,mark,QuotationMark.ESC_DOUBLE);
    }

    public Inputs(QuotationMark mark)
    {
        this(null,mark);
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
            addElements();
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
        return js_call(this.mark,"nova.remote.post",action,getContent(),async);
    }

    public String escapeString(String string)
    {
        if (this.innerMark==null)
        {
            return string;
        }
        String escape=this.innerMark.toString();
        StringBuilder sb=new StringBuilder();
        boolean inString=false;
        for (int i=0;i<string.length();i++)
        {
            char c=string.charAt(i);
            switch (c)
            {
                case '"':
                    sb.append(escape);
                    inString=!inString;
                    break;

                case '\\':
                    sb.append(c);
                    int next=i+1;
                    if (next<string.length())
                    {
                        sb.append(string.charAt(next));
                    }
                    break;
                    
                default:
                    sb.append(c);
            }
            
        }
        return sb.toString();
    }
    
    private String js_call(QuotationMark mark,String function,Object...parameters)
    {
        StringBuilder sb=new StringBuilder(function+"(");
        boolean commaNeeded=false;
        for (Object parameter:parameters)
        {
            if (commaNeeded==false)
            {
                commaNeeded=true;
            }
            else
            {
                sb.append(',');
            }
            if (parameter==null)
            {
                sb.append("null");
            }
            else 
            {
                Class<?> type=parameter.getClass();
                boolean isArray=type.isArray();
                if (isArray)
                {
                    type=type.getComponentType();
                }
                if (type==String.class)
                {
                    if (isArray)
                    {
                        sb.append('[');
                        for (int i=0;i<Array.getLength(parameter);i++)
                        {
                            if (i>0)
                            {
                                sb.append(',');
                            }
                            sb.append(mark.toString()+escapeString(Array.get(parameter, i).toString())+mark.toString());
                        }
                        sb.append(']');
                    }
                    else
                    {
                        sb.append(mark.toString()+escapeString(parameter.toString())+mark.toString());
                    }
                }
                else if ((type==byte.class)
                        ||(type==short.class)
                        ||(type==int.class)
                        ||(type==long.class)
                        ||(type==float.class)
                        ||(type==double.class)
                        ||(type==boolean.class)
                        ||(type==BigDecimal.class)
                        ||(type==Byte.class)
                        ||(type==Short.class)
                        ||(type==Integer.class)
                        ||(type==Long.class)
                        ||(type==Float.class)
                        ||(type==Double.class)
                        ||(type==Boolean.class)
                        )
                {
                    if (isArray)
                    {
                        sb.append('[');
                        for (int i=0;i<Array.getLength(parameter);i++)
                        {
                            if (i>0)
                            {
                                sb.append(',');
                            }
                            sb.append(Array.get(parameter, i));
                        }
                        sb.append(']');
                    }
                    else
                    {
                        sb.append(parameter);
                    }
                }
                else
                {
                    if (isArray)
                    {
                        sb.append('[');
                        for (int i=0;i<Array.getLength(parameter);i++)
                        {
                            if (i>0)
                            {
                                sb.append(',');
                            }
                            sb.append(mark.toString()+Array.get(parameter, i).toString()+mark.toString());
                        }
                        sb.append(']');
                    }
                    else
                    {
                        sb.append(mark.toString()+parameter.toString()+mark.toString());
                    }
                }
            }
        }
        sb.append(");");
        return sb.toString();
    }
    
    public String js_get(String action,boolean async) throws Throwable
    {
        if (this.data==null)
        {
            this.data=ObjectMapper.writeObjectToString(this.inputs.toArray(new Input[this.inputs.size()]));
        }
        return js_call(this.mark,"nova.remote.get",action,getContent(),async);
    }

    public String js_action() throws Throwable
    {
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
