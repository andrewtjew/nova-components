package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.tags.div;
import org.nova.html.tags.script;

public class Refresher extends Element
{
    final private script script;
    final private GlobalTagElement<?> element;
    public Refresher(GlobalTagElement<?> container,String id,String url,long interval)
    {
        this.script=new script().addInner("window.setInterval(function(){$.get('"+url+"',function(data){$('#"+id+"').html(data);});},"+interval+");");
        container.id(id);
        this.element=container;
    }

    public Refresher(String id,String url,long interval)
    {
        this(new div(),id,url,interval);
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
        composer.render(this.element);
        
    }

}
