package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.ButtonStyle;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;

public class LinkButton extends Element
{
    final private a button;
    final private String label;
    private boolean outline=false;
    private ButtonStyle buttonStyle;
    private ButtonSize buttonSize;
    private ButtonState buttonState;
    private boolean block;
    
    public LinkButton(String label)
    {
        this.label=label;
        this.button=new a().attr("role","button").addInner(label);
    }
    
    public LinkButton buttonStyle(ButtonStyle value)
    {
        this.buttonStyle=value;
        return this;
    }
    
    public LinkButton outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public LinkButton block(boolean value)
    {
        this.block=value;
        return this;
    }
    
    public LinkButton buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    
    public LinkButton buttonState(ButtonState value)
    {
        this.buttonState=value;
        return this;
    }
    
    

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("btn");
        cb.add("btn");
        if (this.outline)
        {
            cb.addFragments("outline");
        }
        cb.add("btn",buttonSize);
        cb.add(this.buttonState);
        if (this.block)
        {
            cb.add("btn-block");
        }
        
        cb.addFragments(this.buttonStyle);
        cb.applyTo(this.button);
        composer.render(this.button);
    }
    
    public a button()
    {
        return this.button;
    }
}
