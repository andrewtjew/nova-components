package org.nova.html.ext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.html.elements.Element;
import org.nova.html.elements.FormElement;
import org.nova.html.elements.InputElement;
import org.nova.html.elements.StringComposer;
import org.nova.html.elements.TagElement;
import org.nova.http.client.PathAndQuery;
import org.nova.http.server.Context;
import org.nova.json.ObjectMapper;

public class HtmlUtils
{
    public static String passwordPeekScript(String id)
    {
        return "var x = document.getElementById('"+id+"');if (x.type === 'password') {x.type = 'text';} else {x.type = 'password';}";
    }
    public static String passwordPeekScript(String id,String toggle,String show,String hide)
    {
        return "var x = document.getElementById('"+id+"');var t=document.getElementById('"+toggle+"');if (x.type === 'password') {x.type = 'text';t.innerHTML='"+hide+"';} else {x.type = 'password';t.innerHTML='"+show+"';}";
    }
    /*
    public static String autoId(GlobalTagElement<?> element)
    {
        String id=generateId(element);
        element.id(id);
        return id;
    }
    public static String generateId(Element element)
    {
        return "_"+element.hashCode();
    }
    */
    
    public static String toHtmlText(Element element) throws Throwable
    {
        StringComposer composer=new StringComposer();
        composer.compose(element);
        return composer.getStringBuilder().toString();
    }
    
    public static String location(PathAndQuery builder)
    {
        return "window.location='"+builder.toString()+"'";
    }
    public static String location(String url)
    {
        return "window.location='"+url+"'";
    }
    public static String confirmPOST(String title,String text,PathAndQuery post,Object content,PathAndQuery success) throws Throwable
    {
        String data=content==null?null:ObjectMapper.writeObjectToString(content);
        return callFunction("confirmPOST",title,text,post.toString(),data,success.toString());
    }
    public static String confirmAndExecuteOnServer(String title,String text,String executeUrl,Object content) throws Throwable
    {
        String data=content==null?null:ObjectMapper.writeObjectToString(content);
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
    
    public static List<String> getSelectionNames(Context context,String prefix)
    {
        ArrayList<String> names=new ArrayList<>();
        if (context==null)
        {
            return null;
        }
        for (Entry<String, String[]> entry:context.getHttpServletRequest().getParameterMap().entrySet())
        {
            String[] values=entry.getValue();
            if ((values.length==1)&&("false".equals(values[0])==false))
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

    public static boolean isSelected(Context context,String value)
    {
        if (context==null)
        {
            return false;
        }
        for (Entry<String, String[]> entry:context.getHttpServletRequest().getParameterMap().entrySet())
        {
            String[] values=entry.getValue();
            if ((values.length==1)&&("false".equals(values[0])==false))
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

    public static boolean containsParameter(Context context,String name)
    {
        return context.getHttpServletRequest().getParameterMap().containsKey(name);
    }

    public static List<Long> getSelectionLongList(Context context,String prefix)
    {
        ArrayList<Long> longs=new ArrayList<>();
        if (context==null)
        {
            return null;
        }
        for (String name:getSelectionNames(context, prefix))
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

    public static Map<Long,Long> getSelectionLongLongMap(Context context,String prefix)
    {
        if (context==null)
        {
            return null;
        }
        HashMap<Long,Long> map=new HashMap<>();
        for (Entry<String, String[]> entry:context.getHttpServletRequest().getParameterMap().entrySet())
        {
            String[] values=entry.getValue();
            if (values.length==1)
            {
                String name=null;
                if (prefix==null)
                {
                    name=entry.getKey();
                }
                else
                {
                    String key=entry.getKey();
                    if (key.startsWith(prefix))
                    {
                        name=key.substring(prefix.length());
                    }
                }
                if (name!=null)
                {
                    map.put(Long.parseLong(name), Long.parseLong(values[0]));
                }
                
            }
        }
        return map;
    }
    
    public static String toStringParameter(String string)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<string.length();i++)
        {
            char c=string.charAt(i);
            switch (c)
            {
                case '"':
                    sb.append("&#34;");
                    break;
                    
                case '\'':
                    sb.append("\\'");
                    break;
                    
                case '\\':
                    sb.append("\\\\");
                    break;
                    
                    
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String toReturnStringParameter(String string)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<string.length();i++)
        {
            char c=string.charAt(i);
            switch (c)
            {
                case '\'':
                    sb.append("\\'");
                    break;
                    
                case '\\':
                    sb.append("\\\\");
                    break;
                    
                    
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
    
    
    public static String callSubmit(FormElement<?> form)
    {
        return "document.getElementById('"+form.id()+"').submit();";
    }
    
    public static String callFunction(String function,Object...parameters)
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
                if (type==String.class)
                {
                    sb.append("'"+toStringParameter(parameter.toString())+"'");
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
    
    public static String delayCallFunction(long delay,String function,Object...parameters)
    {
        String call=callFunction(function, parameters);
        return "setTimeout(function(){"+call+"},"+delay+");";
    }
        

    public static String returnFunction(String function,Object...parameters)
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
                if (type==String.class)
                {
                    sb.append("'"+toReturnStringParameter(parameter.toString())+"'");
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

    public static String toHtmlText(String text)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<text.length();i++)
        {
            char c=text.charAt(i);
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
                sb.append("&#39;");
            }
            else 
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String copyToClipboardFunction(TagElement<?> element)
    {
        return "var copyText=getElementById('"+element.id()+"');copyText.select();document.execCommand('Copy');";
    }
    public static String scollIntoView(String id)
    {
        return "getElementById('"+id+"').scrollIntoView({'behavior':'smooth','block':'start'});";
//        return "alert('hello');$('htlm,body').animate({'scrollTop':$('#"+id+"').offset().top}, 2000);alert('world');";
        
    }

    public static void onclickToggleDisable(InputElement<?> source,TagElement<?> target)
    {
        source.onclick("document.getElementById('"+target.id()+"').disabled=this.checked;");
    }
    
    /*
    public static String disableSubmitFunction()
    {
        return "return !(window.event && window.event.keyCode == 13);";
    }
    */
    
    
}
