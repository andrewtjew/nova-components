package org.nova.html.ext;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.tags.div;
import org.nova.html.tags.script;

public class Refresher extends Element
{
    final private script script;
    final private GlobalTagElement<?> element;
    public Refresher(GlobalTagElement<?> container,String url,long interval,String timerName)
    {
        String id=container.id();
        if (id==null)
        {
            id="_"+container.hashCode();
            container.id(id);
        }
        StringBuilder sb=new StringBuilder();
        if (timerName!=null)
        {
            sb.append("var "+timerName+"=");
        }
        sb.append("window.setInterval(function(){$.get('"+url+"',function(data){$('#"+id+"').html(data);});},"+interval+");");
        this.script=new script().addInner(sb.toString());
        this.element=container;
    }

    public Refresher(String id,String url,long interval,String timerName)
    {
        this(new div().id(id),url,interval,timerName);
    }
    public Refresher(String url,long interval,String timerName)
    {
        this(new div(),url,interval,timerName);
    }
    public Refresher(String url,long interval)
    {
        this(url,interval,null);
    }

    public Refresher setInitial(Element element)
    {
        this.element.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.getStringBuilder().append(script.toString());
        composer.compose(this.element);
        
    }

}
