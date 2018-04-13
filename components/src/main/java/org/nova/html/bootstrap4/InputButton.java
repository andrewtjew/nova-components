package org.nova.html.bootstrap4;

/*
import org.nova.html.attributes.input_type;
import org.nova.html.bootstrap4.classes.Size;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.ButtonStyle;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.input;

public class InputButton extends Element
{
    final private input button;
    final private String label;
    private boolean outline=false;
    private ButtonStyle buttonStyle;
    private Size buttonSize;
    private ButtonState buttonState;
    private boolean block;
    
    public InputButton(String label)
    {
        this.label=label;
        this.button=new input().type(input_type.button).addInner(label);
    }
    
    public InputButton buttonStyle(ButtonStyle value)
    {
        this.buttonStyle=value;
        return this;
    }
    
    public InputButton outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public InputButton block(boolean value)
    {
        this.block=value;
        return this;
    }
    
    public InputButton buttonSize(Size value)
    {
        this.buttonSize=value;
        return this;
    }
    
    public InputButton buttonState(ButtonState value)
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
*/