/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.html.ext;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.html.elements.Element;
import org.nova.html.elements.FormElement;
import org.nova.html.elements.InputElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.elements.StringComposer;
import org.nova.html.elements.TagElement;
import org.nova.html.tags.script;
import org.nova.http.client.PathAndQuery;
import org.nova.http.server.Context;
import org.nova.json.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

public class HtmlUtils
{
    //Fixes IE
    public static script ie() 
    {
        return new script().addInner("Object.keys = function (obj) {var keys = [];for (var i in obj) {if (obj.hasOwnProperty(i)) {keys.push(i);}}return keys;};");
    }
    public static String js_peekPassword(String id)
    {
        return "var x = document.getElementById('"+id+"');if (x.type === 'password') {x.type = 'text';} else {x.type = 'password';}";
    }
    public static String js_peekPassword(String id,String toggleElementId,String toggleShowText,String toggleHideText)
    {
        return "var x = document.getElementById('"+id+"');var t=document.getElementById('"+toggleElementId+"');if (x.type === 'password') {x.type = 'text';t.innerHTML='"+toggleHideText+"';} else {x.type = 'password';t.innerHTML='"+toggleShowText+"';}";
    }
    
    public static String js_location(PathAndQuery pathAndQuery)
    {
        return js_location(QuotationMark.DOUBLE,pathAndQuery);
    }
    public static String js_location(QuotationMark mark,PathAndQuery pathAndQuery)
    {
        return "window.location="+mark+pathAndQuery.toString()+mark;
    }

    public static String js_location(QuotationMark mark,String url)
    {
        return "window.location="+mark+url+mark;
    }
    public static String js_location(String url)
    {
        return js_location(QuotationMark.DOUBLE,url);
    }
    public static String js_disableElementAfterCall(String code)
    {
        return "(function(){this.disabled=true;"+code+";})();";
    }
    public static String confirmPOST(String title,String text,PathAndQuery post,Object content,PathAndQuery success) throws Throwable
    {
        String data=content==null?null:ObjectMapper.writeObjectToString(content);
        return js_call("confirmPOST",title,text,post.toString(),data,success.toString());
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
    
    public static String escape(String string)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<string.length();i++)
        {
            char c=string.charAt(i);
            switch (c)
            {
                case '"':
                    sb.append("&quot;");
                    break;

                case '\'':
                    sb.append("&#39;");
                    break;
                    
                case '&':
                    sb.append("&amp;");
                    break;
                    
                case '<':
                    sb.append("&lt;");
                    break;

                case '>':
                    sb.append("&gt;");
                    break;
                    
                    
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
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
                    sb.append("\\\"");
                    break;
                    
                case '\'':
                    sb.append("&#39;");
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
    

    public static String escapeString(char escapeChar,String string)
    {
        StringBuilder sb=new StringBuilder();
//        if (escapeChar=='"')
            if (true)
        {
            for (int i=0;i<string.length();i++)
            {
                char c=string.charAt(i);
                switch (c)
                {
                    case '"':
                        sb.append("\\\"");
                        break;
                        
                    case '\'':
                        sb.append("&#39;");
                        break;
                        
                    case '\\':
                        sb.append("\\\\");
                        break;
                        
                        
                    default:
                        sb.append(c);
                }
                
            }
        }
        else
        {
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
    
    
    public static String js_submit(FormElement<?> form)
    {
        return "document.getElementById('"+form.id()+"').submit();";
    }
    
    public static String js_callWithDelay(long delay,String function,Object...parameters)
    {
        String call=js_call(function, parameters);
        return "setTimeout(function(){"+call+"},"+delay+");";
    }
        
    public static String js_call(String function,Object...parameters)
    {
        return js_call(QuotationMark.DOUBLE,function,parameters);
    }
    
    public static String js_call(QuotationMark mark,String function,Object...parameters)
    {
        char escapeChar=QuotationMark.asChar(mark);
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
                            sb.append(mark.toString()+escapeString(escapeChar,Array.get(parameter, i).toString())+mark.toString());
                        }
                        sb.append(']');
                    }
                    else
                    {
                        sb.append(mark.toString()+escapeString(escapeChar,parameter.toString())+mark.toString());
                    }
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
    public static String escapeQuotes(String text)
    {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<text.length();i++)
        {
            char c=text.charAt(i);
            if (c=='"')
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
    
    public static String js_copyToClipboard(TagElement<?> element)
    {
        return "var copyText=getElementById('"+element.id()+"');copyText.select();document.execCommand('Copy');";
    }
    public static String js_scollIntoView(String id)
    {
        return "getElementById('"+id+"').scrollIntoView({'behavior':'smooth','block':'start'});";
//        return "alert('hello');$('htlm,body').animate({'scrollTop':$('#"+id+"').offset().top}, 2000);alert('world');";
        
    }

    /*
    public static void onclickToggleDisable(InputElement<?> source,TagElement<?> target)
    {
        source.onclick("document.getElementById('"+target.id()+"').disabled=this.checked;");
    }
    
    public static String disableSubmitFunction()
    {
        return "return !(window.event && window.event.keyCode == 13);";
    }
    */
    
    
}
