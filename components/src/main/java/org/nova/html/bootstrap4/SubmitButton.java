package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.ButtonStyle;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.properties.input_type;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.input;

public class SubmitButton extends Element
{
    final private input button;
    final private String label;
    private boolean outline=false;
    private ButtonStyle buttonStyle;
    private ButtonSize buttonSize;
    private ButtonState buttonState;
    private boolean block;
    
    public SubmitButton(String label)
    {
        this.label=label;
        this.button=new input().type(input_type.submit).addInner(label);
    }
    
    public SubmitButton buttonStyle(ButtonStyle value)
    {
        this.buttonStyle=value;
        return this;
    }
    
    public SubmitButton outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public SubmitButton block(boolean value)
    {
        this.block=value;
        return this;
    }
    
    public SubmitButton buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    
    public SubmitButton buttonState(ButtonState value)
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
    
    public input button()
    {
        return this.button;
    }
}
