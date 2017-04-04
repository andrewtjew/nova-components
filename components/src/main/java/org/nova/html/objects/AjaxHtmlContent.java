package org.nova.html.objects;

import java.util.ArrayList;

import org.nova.core.KeyValue;

public class AjaxHtmlContent
{
    final private String id;
    final private String text;
    final private String url;
    private boolean async=true;
    private String type="get";
    private ArrayList<KeyValue<String,String>> queryInputs;
    
    public AjaxHtmlContent(String id,String text,String url)
    {
        this.id=id;
        this.text=text;
        this.url=url;
        this.queryInputs=new ArrayList<>();
    }
    public AjaxHtmlContent async(boolean async)
    {
        this.async=async;
        return this;
    }
    public AjaxHtmlContent type(String type)
    {
        this.type=type;
        return this;
    }
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
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
}