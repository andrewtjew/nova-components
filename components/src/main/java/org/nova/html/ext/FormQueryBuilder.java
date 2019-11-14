package org.nova.html.ext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.nova.html.elements.InputElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.elements.TagElement;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_hidden;
import org.nova.html.tags.input_number;
import org.nova.html.tags.input_radio;
import org.nova.html.tags.input_text;
import org.nova.html.tags.script;
import org.nova.html.tags.select;
import org.nova.http.client.PathAndQuery;

public class FormQueryBuilder
{
//    final static String QUOTE="&#34;";
    final private String QUOTE;
    final private FormQueryBuilder parent;
    final private StringBuilder sb=new StringBuilder();
    
    public FormQueryBuilder(QuotationMark mark)
    {
    	this(mark,null);
    }

    public FormQueryBuilder()
    {
        this(QuotationMark.SINGLE);
    }

    public FormQueryBuilder(QuotationMark mark,FormQueryBuilder parent)
    {
        this.parent=parent;
        this.QUOTE=mark.toString();
    }
    public FormQueryBuilder(FormQueryBuilder parent)
    {
    	this(QuotationMark.SINGLE,parent);
    }
    
    private void addName(String name)
    {
        if (this.sb.length()>0)
        {
            sb.append("+"+QUOTE+"&");
        }
        sb.append(name);
        sb.append("="+QUOTE+"+");
    }
    /*
    public FormQueryBuilder addValueQuery(String name,Object value) throws Throwable
    {
        if (value==null)
        {
            return this;
        }
        addName(name);
        this.sb.append(QUOTE+value+QUOTE);
        return this;
    }
    */
    
    public FormQueryBuilder value(String name,String id,String value)
    {
        addName(name);
        this.sb.append("document.getElementById("+QUOTE+id+QUOTE+")."+value);
        return this;
    }
    
    public FormQueryBuilder add(String name,Object value) throws UnsupportedEncodingException
    {
        if (value==null)
        {
            return this;
        }
        if (this.sb.length()>0)
        {
            sb.append("+"+QUOTE+"&");
        }
        sb.append(name);
        sb.append("=");
        this.sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
        this.sb.append(QUOTE);
        return this;
    }
    public FormQueryBuilder checked(InputElement<?> element)
    {
        return value(element.name(),element.id(),"checked");
    }
    public FormQueryBuilder checked(input_checkbox element)
    {
        return value(element.name(),element.id(),"checked");
    }
    public FormQueryBuilder checked(input_radio element)
    {
        return value(element.name(),element.id(),"checked");
    }
    public FormQueryBuilder value(InputElement<?> element)
    {
        return value(element.name(),element.id(),"value");
    }
    public FormQueryBuilder value(input_number element)
    {
        return value(element.name(),element.id(),"value");
    }
    public FormQueryBuilder value(input_text element)
    {
        return value(element.name(),element.id(),"value");
    }
    public FormQueryBuilder value(input_hidden element)
    {
        return value(element.name(),element.id(),"value");
    }
    public FormQueryBuilder value(String name,TagElement<?> element)
    {
        return value(name,element.id(),"value");
    }
    public FormQueryBuilder selected(select element)
    {
        String document="document.getElementById("+QUOTE+element.id()+QUOTE+")";
        addName(element.name());
        this.sb.append(document+".options["+document+".selectedIndex].value");
        return this;
    }
    public FormQueryBuilder selected(String name,TagElement<?> element)
    {
        String document="document.getElementById("+QUOTE+element.id()+QUOTE+")";
        addName(name);
        this.sb.append(document+".options["+document+".selectedIndex].value");
        return this;
    }

    /*
    public String generateFormQuery(String path)
    {
        return 
    }
    */

    public String js_query(PathAndQuery pathAndQuery)
    {
        if (this.sb.length()==0)
        {
            if (this.parent!=null)
            {
                return this.parent.js_query(pathAndQuery);
            }
            return QUOTE+pathAndQuery.toString()+QUOTE;
        }
        if (this.parent!=null)
        {
            if (this.parent.sb.length()>0)
            {
                if (this.sb.length()==0)
                {
                    return this.parent.js_query(pathAndQuery);
                }
                return this.parent.js_query(pathAndQuery)+'+'+QUOTE+'&'+this.sb.toString();
            }
        }
        if (pathAndQuery.containQueries())
        {
            return QUOTE+pathAndQuery.toString()+"&"+this.sb.toString();
        }
        return QUOTE+pathAndQuery.toString()+"?"+this.sb.toString();
    }

    
//    public FormQueryBuilder onClickLocation(String path,GlobalEventTagElement<?> element)
//    {
//        element.onclick("window.location="+generateFormQuery(path));
//        return this;
//    }
//
//    public FormQueryBuilder onClickCall(String function,String path,GlobalEventTagElement<?> element)
//    {
//        element.onclick(function+"("+generateFormQuery(path)+")");
//        return this;
//    }

    public String js_location(PathAndQuery pathAndQuery)
    {
        return "window.location="+js_query(pathAndQuery);
    }

    public String js_call(String function,PathAndQuery pathAndQuery)
    {
        return function+"("+js_query(pathAndQuery)+")";
    }
    
    public script generateScript(String functionName,PathAndQuery pathAndQuery)
    {
        String text="function "+functionName+"(){"+js_location(pathAndQuery)+";}";
        return new script().addInner(text);
    }
    
}
