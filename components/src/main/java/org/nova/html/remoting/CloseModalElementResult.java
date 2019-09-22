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
