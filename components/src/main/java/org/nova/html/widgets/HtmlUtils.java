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
        return callScriptFunction("confirmPOST",title,text,post.toString(),data,success.toString());
    }
    public static String confirmAndExecuteOnServer(String title,String text,String executeUrl,Object content) throws Exception
    {
        String data=content==null?null:ObjectMapper.write(content);
        StringBuilder sb=new StringBuilder("confirmAndExecuteOnServer(");
        if (title==null)
        {
            sb.append("null");
        }
        else
        {
            sb.append('\'').append(title).append('\'');
        }
        if (text==null)
        {
            sb.append(",null");
        }
        else
        {
            sb.append(",'").append(text).append('\'');
        }
        if (executeUrl==null)
        {
            sb.append(",null");
        }
        else
        {
            sb.append(",'").append(executeUrl).append('\'');
        }
        if (data==null)
        {
            sb.append(",null);");
        }
        else
        {
            sb.append(",\'").append(data).append("');");
        }
        return sb.toString();
    }
    public static String confirmAndExecuteOnServer(String title,String text,String executeUrl) throws Exception
    {
        return confirmAndExecuteOnServer(title, text, executeUrl,null);
    }
    
    public static String callScriptFunction(String method,Object...parameters)
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
                    sb.append("'"+parameter.toString().replace("'","\\'")+"'");
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
    public static String escapeXmlBrackets(String xmlText)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<xmlText.length();i++)
        {
            char c=xmlText.charAt(i);
            if (c=='<')
            {
                sb.append("&lt");
            }
            else if (c=='>')
            {
                sb.append("&gt");
            }
            else 
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
