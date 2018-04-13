package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Size;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;

public class LinkButton extends Element
{
    final private a button;
    private boolean outline=false;
    private ThemeColor color;
    private ThemeColor textColor;
    private Size buttonSize;
    private ButtonState buttonState;
    private boolean block;
    
    public LinkButton(String label,String href)
    {
        this.button=new a().attr("role","button").addInner(label).href(href);
    }
    
    public LinkButton color(ThemeColor value)
    {
        this.color=value;
        return this;
    }
    
    public LinkButton textColor(ThemeColor value)
    {
        this.textColor=value;
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
    
    public LinkButton buttonSize(Size value)
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
        cb.add("form-control");
        cb.add("btn");
        cb.addFragmentsIf(this.outline,"outline");
        cb.addFragments(this.color);

        cb.addIf(this.textColor!=null,"text",this.textColor);
        //        cb.add("btn",buttonSize);
        cb.add(this.buttonState);
        if (this.block)
        {
            cb.add("btn-block");
        }
        
        cb.applyTo(this.button);
        composer.render(this.button);
    }
    
    public a button()
    {
        return this.button;
    }
}
