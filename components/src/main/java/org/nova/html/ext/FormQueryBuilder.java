package org.nova.html.ext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.nova.html.elements.TagElement;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_radio;
import org.nova.html.tags.script;
import org.nova.html.tags.select;

public class FormQueryBuilder
{
//    final static String QUOTE="&#34;";
    final private static String QUOTE="'";
    final private FormQueryBuilder parent;
    final private StringBuilder sb=new StringBuilder();
    
    public FormQueryBuilder()
    {
        this.parent=null;
    }

    public FormQueryBuilder(FormQueryBuilder parent)
    {
        this.parent=parent;
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
    
    public FormQueryBuilder addQuery(String name,String id,String value)
    {
        addName(name);
        this.sb.append("document.getElementById("+QUOTE+id+QUOTE+")."+value);
        return this;
    }
    
    public FormQueryBuilder addValueQuery(String name,Object value) throws UnsupportedEncodingException
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
//    public FormQueryBuilder addQuery(InputElement<?> element,String value)
//    {
//        return addQuery(element.name(),element.id(),"value");
//    }
//    
    public FormQueryBuilder addQuery(input_checkbox element)
    {
        return addQuery(element.name(),element.id(),"checked");
    }
    public FormQueryBuilder addCheckedQuery(String name,TagElement<?> element)
    {
        return addQuery(name,element.id(),"checked");
    }
    public FormQueryBuilder addQuery(String name,TagElement<?> element)
    {
        return addQuery(name,element.id(),"value");
    }
    public FormQueryBuilder addQuery(input_radio element)
    {
        return addQuery(element.name(),element.id(),"checked");
    }
    public FormQueryBuilder addQuery(select element)
    {
        String document="document.getElementById("+QUOTE+element.id()+QUOTE+")";
        addName(element.name());
        this.sb.append(document+".options["+document+".selectedIndex].value");
        return this;
    }
    
    public String generateFormQuery(String path)
    {
        if (this.sb.length()==0)
        {
            if (this.parent!=null)
            {
                return this.parent.generateFormQuery(path);
            }
            return QUOTE+path+QUOTE;
        }
        if (this.parent!=null)
        {
            if (this.parent.sb.length()>0)
            {
                if (this.sb.length()==0)
                {
                    return this.parent.generateFormQuery(path);
                }
                return this.parent.generateFormQuery(path)+'+'+QUOTE+'&'+this.sb.toString();
            }
        }
        return QUOTE+path+"?"+this.sb.toString();
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

    public String generateLocation(String path)
    {
        return "window.location="+generateFormQuery(path);
    }

    public String generateCall(String function,String path)
    {
        return function+"("+generateFormQuery(path)+")";
    }
    
    public script generateScript(String functionName,String path)
    {
        String text="function "+functionName+"(){"+generateLocation(path)+";}";
        return new script().addInner(text);
    }
    
}
