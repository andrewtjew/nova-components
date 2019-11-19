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
package org.nova.html.remoting;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.StringComposer;
import org.nova.html.elements.TagElement;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.Text;

public class CloseModalElementResult extends Result
{
    final StringBuilder sb;
    
    public CloseModalElementResult(String parentId,Element element)
    {
        this.sb=new StringBuilder();
        add(parentId,element);
    }
    public CloseModalElementResult(String parentId,Object object)
    {
        this.sb=new StringBuilder();
        add(parentId,object);
    }
    
    public CloseModalElementResult add(String parentId,Element element)
    {
        if (element!=null)
        {
            StringComposer composer=new StringComposer();
            sb.append("document.getElementById(\""+parentId+"\").innerHTML=\""+composer.getStringBuilder().toString()+"\";");
        }
        return this;
    }
    public CloseModalElementResult add(String parentId,Object object)
    {
        if (object!=null)
        {
            add(parentId, new Text(object));
        }
        return this;
    }
    
    @Override
    public String serialize() throws Throwable
    {
        return this.sb.toString();
    }
    
    /*
    public static String generateGetMethod(String path,FormQueryBuilder formQueryBuilder)
    {
        return "$.get({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});";
    }
    public static void onClickGet(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder)
    {
        element.onclick("$.get({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickCloseAndGet(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,String modalId)
    {
        element.onclick("$(\"#"+modalId+"\").modal(\"hide\");$.get({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickCloseAndGet(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,TagElement<?> modal)
    {
        onClickCloseAndGet(element,path,formQueryBuilder,modal.id());
    }
    public static void onClickOpenAndGet(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,String modalId)
    {
        element.onclick("$(\"#"+modalId+"\").modal(\"show\");$.get({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickOpenAndGet(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,TagElement<?> modal)
    {
        onClickOpenAndGet(element,path,formQueryBuilder,modal.id());
    }
    
    public static void onClickPost(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder)
    {
        element.onclick("$.post({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickCloseAndPost(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,String modalId)
    {
        element.onclick("$(\"#"+modalId+"\").modal(\"hide\");$.post({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickCloseAndPost(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,TagElement<?> modal)
    {
        onClickCloseAndPost(element,path,formQueryBuilder,modal.id());
    }
    public static void onClickOpenAndPost(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,String modalId)
    {
        element.onclick("$(\"#"+modalId+"\").modal(\"show\");$.post({url:"+formQueryBuilder.generateFormQuery(path)+",success:function(script){eval(script);}});");
    }
    public static void onClickOpenAndPost(GlobalEventTagElement<?> element,String path,FormQueryBuilder formQueryBuilder,TagElement<?> modal)
    {
        onClickOpenAndPost(element,path,formQueryBuilder,modal.id());
    }
    */
    
}
