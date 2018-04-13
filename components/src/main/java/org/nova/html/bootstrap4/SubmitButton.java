
package org.nova.html.bootstrap4;

import org.nova.html.attributes.input_type;
import org.nova.html.bootstrap4.classes.Size;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.input;
import org.nova.html.tags.input_submit;

public class SubmitButton extends Element
{
    final private input_submit button;
    private boolean outline=false;
    private ThemeColor color;
    private Size size;
    private ButtonState buttonState;
    private boolean block;
    
    public SubmitButton(String label)
    {
        this.button=new input_submit().value(label);
    }
    
    public SubmitButton color(ThemeColor value)
    {
        this.color=value;
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
    
    public SubmitButton size(Size value)
    {
        this.size=value;
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
        cb.add("form-control");
        cb.add("btn");
        cb.addFragmentsIf(this.outline,"outline");
        cb.addFragments(this.color);

        cb.addIf(this.size!=null,"btn",size);
        cb.add(this.buttonState);
        cb.addIf(this.block,"btn-block");

        cb.applyTo(this.button);
        composer.render(this.button);
    }
    
}
