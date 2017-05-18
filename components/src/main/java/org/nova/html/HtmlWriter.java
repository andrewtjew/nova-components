package org.nova.html;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.jar.Attributes;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;

public class HtmlWriter extends Element
{
	final private StringBuilder sb;
	public HtmlWriter()
	{
		this.sb=new StringBuilder();
	}
	public HtmlWriter tag(String tag)
	{
		sb.append("<"+tag+"/>");
		return this;
	}
	public HtmlWriter begin_tag(String tag,Attribute...attributes)
	{
		sb.append("<"+tag);
		for (Attribute attribute:attributes)
		{
			sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
		}
		sb.append(">");
		return this;
	}
    public HtmlWriter begin_tag(String tag,Attribute[] userAttributes,Attribute...constantAttributes)
    {
        sb.append("<"+tag);
        for (Attribute attribute:constantAttributes)
        {
            sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
        }
        for (Attribute attribute:userAttributes)
        {
            sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
        }
        sb.append(">");
        return this;
    }
	public HtmlWriter end_tag(String tag)
	{
		sb.append("</"+tag+">");
		return this;
	}
	
    private HtmlWriter tag(String tag,Object value,Attribute[] userAttributes,Attribute...constantAttributes)
    {
        sb.append("<"+tag);
        for (Attribute attribute:constantAttributes)
        {
            sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
        }
        for (Attribute attribute:userAttributes)
        {
            sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
        }
        if (value!=null)
        {
            sb.append(">");
            sb.append(value);
            sb.append("</"+tag+">");
        }
        else
        {
            sb.append("/>");
        }
        return this;
    }
	
	public HtmlWriter tag(String tag,Object value,Attribute...attributes)
	{
		sb.append("<"+tag);
		for (Attribute attribute:attributes)
		{
            sb.append(" "+attribute.getName());
		    if (attribute.getValue()!=null)
		    {
		        sb.append("=\""+attribute.getValue()+"\"");
		    }
		}
		if (value!=null)
		{
			sb.append(">");
			sb.append(value);
			sb.append("</"+tag+">");
		}
		else
		{
			sb.append("/>");
		}
		return this;
	}
	public HtmlWriter inner()
	{
		return new HtmlWriter();
	}
	
	public HtmlWriter begin_html()
	{
		return begin_tag("html");
	}
	public HtmlWriter end_html()
	{
		return begin_tag("html");
	}
	public HtmlWriter html(Object value)
	{
		return tag("html",value);
	}
	public HtmlWriter br()
	{
		sb.append("<br>");
		return this;
	}
	public HtmlWriter hr()
	{
		sb.append("<hr>");
		return this;
	}
	public HtmlWriter h1(Object value,Attribute...attributes)
	{
		return tag("h1",value,attributes);
	}
	
	public HtmlWriter h2(Object value,Attribute...attributes)
	{
		return tag("h2",value,attributes);
	}
	
	public HtmlWriter h3(Object value,Attribute...attributes)
	{
		return tag("h3",value,attributes);
	}
	
	public HtmlWriter h4(Object value,Attribute...attributes)
	{
		return tag("h4",value,attributes);
	}
	
	public HtmlWriter h5(Object value,Attribute...attributes)
	{
		return tag("h5",value,attributes);
	}
	
    public HtmlWriter p()
    {
        return tag("p",null);
    }
	public HtmlWriter p(Object value,Attribute...attributes)
	{
		return tag("p",value,attributes);
	}
	
	public HtmlWriter option(Object value,Attribute...attributes)
	{
		return tag("option",value,attributes);
	}
	public HtmlWriter div(Object value,Attribute...attributes)
	{
		return tag("div",value,attributes);
	}

	public HtmlWriter textarea(Object value,Attribute...attributes)
	{
		return tag("textarea",value,attributes);
	}
    public HtmlWriter script(Object value,Attribute...attributes)
    {
        return tag("script",value,attributes);
    }

	public HtmlWriter textarea(Object value,boolean readonly,int rows,int cols,Attribute...attributes)
	{
		if (readonly)
		{
			return tag("textarea",value,attributes,new Attribute("rows", rows),new Attribute("cols", cols),new Attribute("readonly"));
		}
		else
		{
			return tag("textarea",value,attributes,new Attribute("rows", rows),new Attribute("cols", cols));
		}
	}
	
	public HtmlWriter em(Object value,Attribute...attributes)
	{
		return tag("em",value,attributes);
	}

	public HtmlWriter small(Object value,Attribute...attributes)
	{
		return tag("small",value,attributes);
	}
	
	public HtmlWriter mark(Object value,Attribute...attributes)
	{
		return tag("mark",value,attributes);
	}

	public HtmlWriter del(Object value,Attribute...attributes)
	{
		return tag("del",value,attributes);
	}

	public HtmlWriter ins(Object value,Attribute...attributes)
	{
		return tag("ins",value,attributes);
	}

	public HtmlWriter sub(Object value,Attribute...attributes)
	{
		return tag("sub",value,attributes);
	}

	public HtmlWriter sup(Object value,Attribute...attributes)
	{
		return tag("sup",value,attributes);
	}

	public HtmlWriter strong(Object value,Attribute...attributes)
	{
		return tag("strong",value,attributes);
	}


	public HtmlWriter canvas(Object value,Attribute...attributes)
	{
		return tag("canvas",value,attributes);
	}
	
	public HtmlWriter a(Object value,Attribute...attributes)
	{
		return tag("a",value,attributes);
	}
	
	public HtmlWriter a(Object value,String href,Attribute...attributes)
	{
	    return tag("a",value,attributes,new Attribute("href",href));
	}

	public HtmlWriter table(Object value,Attribute...attributes)
	{
		return tag("table",value,attributes);
	}
	
	public HtmlWriter th(Object value,Attribute...attributes)
	{
		return tag("th",value,attributes);
	}

	public HtmlWriter th()
	{
		return tag("th");
	}
	
	public HtmlWriter tr(Object value,Attribute...attributes)
	{
		return tag("tr",value,attributes);
	}
	public HtmlWriter tr()
	{
		return tag("tr");
	}
	
	public HtmlWriter th(Object value,String title)
	{
		return tag("th",value,new Attribute("title",title));
	}
	
	public HtmlWriter text(Object text,Attribute...attributes)
	{
	    return tag("text",text,attributes);
	}
	
    public HtmlWriter writeObject(Object object)
    {
        sb.append(object);
        return this;
    }
	public HtmlWriter textLine(String text)
	{
		sb.append(text).append("<br/>");
		return this;
	}
	
	public HtmlWriter td(Object value,Attribute...attributes)
	{
		return tag("td",value,attributes);
	}
	
	public HtmlWriter td(Object value,String title)
	{
		return tag("td",value,new Attribute("title",title));
	}

	public HtmlWriter td()
	{
		return tag("td");
	}

	public HtmlWriter thead(Object value,Attribute...attributes)
	{
		return tag("thead",value,attributes);
	}

	public HtmlWriter tfoot(Object value)
	{
		return tag("tfoot",value);
	}

	
	public HtmlWriter begin_table(String class_,int border)
	{
		sb.append("<table border=\""+border+"\"");
		if (class_!=null)
		{
			sb.append(" class=\""+class_+"\"");
		}
		sb.append(">");
		return this;
	}
	public HtmlWriter tableList(Object key,Object value)
	{
		return tr(this.inner().td(key).td(":").td(value));
	}
	
	public HtmlWriter begin_table(int border)
	{
		return begin_table(null,border);
	}
    public HtmlWriter begin_table(Attribute...attributes)
    {
        return begin_tag("table",attributes);
    }
    public HtmlWriter col(String width)
    {
        return tag("col",null,new Attribute("width",width));
    }
    public HtmlWriter col(int width)
    {
        return tag("col",null,new Attribute("width",width));
    }
	
    public HtmlWriter begin_form(String action,String method,Attribute...attributes)
    {
        return begin_tag("form",attributes,new Attribute("action",action),new Attribute("method",method));
    }

    public HtmlWriter begin_form(String action,String method,String idAndName,Attribute...attributes)
    {
       return begin_tag("form",attributes,new Attribute("action",action),new Attribute("method",method),new Attribute("id",idAndName),new Attribute("name",idAndName));
    }

	public HtmlWriter begin_form(String action)
    {
        return begin_form(action,"get");
    }

	public HtmlWriter end_form()
	{
		sb.append("</form>");
		return this;
	}

    public HtmlWriter button(Object display,Attribute...attributes)
    {
        return tag("button",display,attributes);
    }
    public HtmlWriter button(Object display,String idAndName,Object value,Attribute...attributes)
    {
        return tag("button",display,attributes,new Attribute("id",idAndName),new Attribute("name",idAndName),new Attribute("value",value));
    }
    public HtmlWriter button(Object display,Object value,Attribute...attributes)
    {
        return tag("button",display,attributes,new Attribute("value",value));
    }

    public HtmlWriter input(Object value,Attribute...attributes)
    {
        return tag("input",value,attributes);
    }

    public HtmlWriter input_hidden(String idAndName,Object value)
    {
        return tag("input",null,new Attribute("type","hidden"),new Attribute("id",idAndName),new Attribute("name",idAndName),new Attribute("value",value));
    }
	public HtmlWriter input_text(int size,String idAndName,Object value,Attribute...attributes)
	{
		return tag("input",null,attributes,new Attribute("type","text"),new Attribute("size",size),new Attribute("id",idAndName),new Attribute("name",idAndName),new Attribute("value",value));
	}
    public HtmlWriter input_text(int size,String idAndName,Attribute...attributes)
    {
        return tag("input",null,attributes,new Attribute("type","text"),new Attribute("size",size),new Attribute("id",idAndName),new Attribute("name",idAndName));
    }
    public HtmlWriter input_text(String idAndName,Attribute...attributes)
    {
        return tag("input",null,attributes,new Attribute("type","text"),new Attribute("id",idAndName),new Attribute("name",idAndName));
    }
    public HtmlWriter input_submit(Object value,String name,Attribute...attributes)
    {
        return tag("input",null,attributes,new Attribute("name",name),new Attribute("value",value),new Attribute("type","submit"));
    }
    public HtmlWriter input_submit(String name,Attribute...attributes)
    {
        return tag("input",null,attributes,new Attribute("name",name),new Attribute("type","submit"));
    }
	public HtmlWriter input_checkbox(String idAndName,Object value,boolean checked,Attribute...attributes)
	{
		if (checked)
		{
			return tag("input",value,attributes,new Attribute("type","checkbox"),new Attribute("id",idAndName),new Attribute("name",idAndName),new Attribute("checked"));
		}
        return tag("input",value,attributes,new Attribute("type","checkbox"),new Attribute("id",idAndName),new Attribute("name",idAndName));
	}

	public HtmlWriter begin_sortableTable(int border)
	{
		return begin_table("sortable",border);
	}
	
	public HtmlWriter end_table()
	{
	    return end_tag("table");
	}
	
	public HtmlWriter begin_tr(Attribute...attributes)
	{
		return begin_tag("tr",attributes);
	}

	public HtmlWriter end_tr()
	{
        return end_tag("tr");
	}
	public HtmlWriter next_tr()
	{
		sb.append("</tr><tr>");
		return this;
	}
	public HtmlWriter begin_th(Attribute...attributes)
	{
        return begin_tag("th",attributes);
	}

	public HtmlWriter end_th()
	{
        return end_tag("th");
	}
	public HtmlWriter begin_td(Attribute...attributes)
	{
        return begin_tag("td",attributes);
	}

	public HtmlWriter end_td()
	{
        return end_tag("td");
	}
	public HtmlWriter begin_select(String idAndName,Attribute...attributes)
	{
        return begin_tag("select",attributes,new Attribute("id",idAndName),new Attribute("name",idAndName));
	}

	public HtmlWriter end_select()
	{
		return end_tag("select");
	}

	public HtmlWriter begin_thead(Attribute...attributes)
	{
		return begin_tag("thead",attributes);
	}

	public HtmlWriter end_thead()
	{
		return end_tag("head");
	}

	public HtmlWriter begin_tfoot(Attribute...attributes)
	{
		return begin_tag("tfoot",attributes);
	}

	public HtmlWriter end_tfoot()
	{
		return end_tag("foot");
	}
	public HtmlWriter begin_tbody(Attribute...attributes)
	{
		return begin_tag("tbody",attributes);
	}

	public HtmlWriter end_tbody()
	{
		return end_tag("body");
	}
    public HtmlWriter begin_div(Attribute...attributes)
    {
        return begin_tag("div",attributes);
    }
    public HtmlWriter begin_div(String idAndName,Attribute...attributes)
    {
        return begin_tag("div",attributes,new Attribute("name",idAndName),new Attribute("id",idAndName));
    }

	public HtmlWriter end_div()
	{
		return end_tag("div");
	}

	public String toString()
	{
		return sb.toString();
	}
    @Override
    public void build(Builder builder) throws Throwable
    {
        builder.getOutputStream().write(this.sb.toString().getBytes(StandardCharsets.UTF_8));
    }

}
