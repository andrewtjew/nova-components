package org.nova.html.widgets;

import org.nova.html.enums.http_equiv;

import java.math.BigDecimal;

import org.nova.html.elements.Element;
import org.nova.html.tags.meta;
import org.nova.http.client.PathAndQueryBuilder;
import org.nova.json.ObjectMapper;

public class HtmlUtils
{
    public static String location(PathAndQueryBuilder builder)
    {
        return "window.location='"+builder.toString()+"'";
    }
    public static String location(String url)
    {
        return "window.location='"+url+"'";
    }
    public static Element redirect(String url)
    {
        BasicPage page=new BasicPage();
        page.head().addInner(new meta().http_equiv(http_equiv.refresh).content("0;URL='"+url+"'"));
        return page;
    }
    public static String confirmPOST(String title,String text,PathAndQueryBuilder post,Object content,PathAndQueryBuilder success) throws Exception
    {
        String data=content==null?null:ObjectMapper.write(content);
        return call("confirmPOST",title,text,post.toString(),data,success.toString());
    }
    
    public static String call(String method,Object...parameters)
    {
        StringBuilder sb=new StringBuilder(method+"(");
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
                if (type==String.class)
                {
                    sb.append("'"+parameter+"'");
                }
                else if ((type==byte.class)
                        ||(type==short.class)
                        ||(type==int.class)
                        ||(type==long.class)
                        ||(type==float.class)
                        ||(type==double.class)
                        ||(type==BigDecimal.class)
                        ||(type==Byte.class)
                        ||(type==Short.class)
                        ||(type==Integer.class)
                        ||(type==Long.class)
                        ||(type==Float.class)
                        ||(type==Double.class)
                        )
                {
                    sb.append(parameter);
                }
                else
                {
                    sb.append("'"+parameter.toString()+"'");
                }
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
