package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.AlignItems;
import org.nova.html.bootstrap4.classes.JustifyContent;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.widgets.Content;

public class Row extends Element 
{
    private AlignItems align;
    private JustifyContent justify;
    private boolean noGutters=true;
    final private Content content;
    
    public Row()
	{
        this.content=new Content();
	}
    
    public Row align(AlignItems value)
    {
        this.align=value;
        return this;
    }
    
    public Row justify(JustifyContent value)
    {
        this.justify=value;
        return this;
    }
    
    public Row noGutters(boolean value)
    {
        this.noGutters=value;
        return this;
    }
    
    public Row add(Column col)
    {
        this.content.addInner(col);
        return this;
    }
    
	@Override
    public void compose(Composer composer) throws Throwable
    {
	    ClassBuilder sb=new ClassBuilder("row");
        sb.addIf(this.align!=null,"align-items",this.align);
        sb.addIf(this.justify!=null,"justify_content",this.justify);
        sb.addIf(this.noGutters,"no-gutters");
        
	    div div=new div();
	    sb.applyTo(div);
	    div.addInner(this.content);
	    composer.render(div);
    }
}
