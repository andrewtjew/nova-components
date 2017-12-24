package org.nova.html.widgets;

import org.nova.html.enums.http_equiv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.nova.html.elements.Element;
import org.nova.html.tags.meta;
import org.nova.http.client.PathAndQueryBuilder;
import org.nova.http.server.Context;
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
    public static String confirmPOST(String title,String text,PathAndQueryBuilder post,Object content,PathAndQueryBuilder success) throws Throwable
    {
        String data=content==null?null:ObjectMapper.write(content);
        return callScriptFunction("confirmPOST",title,text,post.toString(),data,success.toString());
    }
    public static String confirmAndExecuteOnServer(String title,String text,String executeUrl,Object content) throws Throwable
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
    public static String confirmAndExecuteOnServer(String title,String text,String executeUrl) throws Throwable
    {
        return confirmAndExecuteOnServer(title, text, executeUrl,null);
    }
    
    public static List<String> getCheckedNames(Context context,String prefix)
    {
        ArrayList<String> names=new ArrayList<>();
        for (Entry<String, String[]> entry:context.getHttpServletRequest().getParameterMap().entrySet())
        {
            String[] values=entry.getValue();
            if ((values.length==1)&&("on".equals(values[0])))
            {
                if (prefix==null)
                {
                    names.add(entry.getKey());
                }
                else
                {
                    String key=entry.getKey();
                    if (key.startsWith(prefix))
                    {
                        names.add(key.substring(prefix.length()));
                    }
                }
            }
        }
        return names;
    }

    public static boolean isChecked(Context context,String value)
    {
        ArrayList<String> names=new ArrayList<>();
        for (Entry<String, String[]> entry:context.getHttpServletRequest().getParameterMap().entrySet())
        {
            String[] values=entry.getValue();
            if ((values.length==1)&&("on".equals(values[0])))
            {
                String key=entry.getKey();
                if (value.equals(key))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Long> getLongCheckedNames(Context context,String prefix)
    {
        ArrayList<Long> longs=new ArrayList<>();
        for (String name:getCheckedNames(context, prefix))
        {
            try
            {
                longs.add(Long.parseLong(name));
            }
            catch (Throwable t)
            {
            }
        }
        return longs;
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
                sb.append("&lt;");
            }
            else if (c=='>')
            {
                sb.append("&gt;");
            }
            else if (c=='&')
            {
                sb.append("&amp;");
            }
            else if (c=='"')
            {
                sb.append("&quot;");
            }
            else if (c=='\'')
            {
                sb.append("&apos;");
            }
            else 
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
