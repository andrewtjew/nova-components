package org.nova.html.objects;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.core.KeyValue;
import org.nova.html.Attribute;
import org.nova.html.elements.Element;

public class AjaxButton extends Element
{
	final private String id;
	final private String text;
	final private String url;
	private boolean async=true;
	private String type="get";
	private ArrayList<KeyValue<String,String>> queryInputs;
	private final Attribute[] attributes;
	
	public AjaxButton(String id,String text,String url,Attribute...attributes)
	{
	    this.attributes=attributes;
		this.id=id;
		this.text=text;
		this.url=url;
		this.queryInputs=new ArrayList<>();
	}
	public AjaxButton async(boolean async)
	{
		this.async=async;
		return this;
	}
	public AjaxButton type(String type)
	{
		this.type=type;
		return this;
	}
	public AjaxButton val(String parameterName,String inputId)
	{
		this.queryInputs.add(new KeyValue<String, String>(parameterName, "':$('#"+inputId+"').val()"));
		return this;
	}
	public AjaxButton prop(String parameterName,String inputId,String prop)
	{
		this.queryInputs.add(new KeyValue<String, String>(parameterName, "':$('#"+inputId+"').prop('"+prop+"')"));
		return this;
	}
	public AjaxButton parameter(String parameterName,String value)
	{
		this.queryInputs.add(new KeyValue<String, String>(parameterName, "':'"+value+"'"));
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("<button");

		for (Attribute attribute:this.attributes)
		{
		    sb.append(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");
		}
		sb.append(" type='button' id='");
		sb.append(this.id);
		sb.append("'>");
		sb.append(this.text);
		sb.append("</button><script type='text/javascript'>$('#"+id+"').click(function(){$.ajax({type:'");
		sb.append(this.type);
		sb.append("',url:'");
		sb.append(this.url);
		sb.append("',dataType:'json',data:{");
		boolean commaNeeded=false;
		for (KeyValue<String,String> item:this.queryInputs)
		{
			if (commaNeeded==false)
			{
				sb.append("'");
				commaNeeded=true;
			}
			else
			{
				sb.append(",'");
			}
			sb.append(item.getName());
			sb.append(item.getValue());
		}
		sb.append("},async:");
		sb.append(async);
		sb.append(",success:function(data){$.each(data,function(key,value){$('#'+key).html(value);})} });});</script>");
		return sb.toString();
	}
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(toString().getBytes(StandardCharsets.UTF_8));
        
    }
}
