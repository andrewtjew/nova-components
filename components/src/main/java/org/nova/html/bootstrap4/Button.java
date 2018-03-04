package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.ButtonStyle;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;

public class Button extends Element
{
    final private button_button button;
    final private String label;
    private boolean outline=false;
    private ButtonStyle buttonStyle;
    private ButtonSize buttonSize;
    private ButtonState buttonState;
    private boolean block;
    private String modelTargetID;
    
    public Button(String label)
    {
        this.label=label;
        this.button=new button_button().addInner(label);
    }
    
    public Button buttonStyle(ButtonStyle value)
    {
        this.buttonStyle=value;
        return this;
    }
    
    public Button outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public Button block(boolean value)
    {
        this.block=value;
        return this;
    }
    
    public Button buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    
    public Button buttonState(ButtonState value)
    {
        this.buttonState=value;
        return this;
    }
    
    public Button modalTarget(String id)
    {
        this.modelTargetID=id;
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
        if (this.modelTargetID!=null)
        {
            this.button.attr("data-toggle","modal");
            this.button.attr("data-target","#"+this.modelTargetID);
        }
        cb.addFragments(this.buttonStyle);
        cb.applyTo(this.button);
        composer.render(this.button);
    }
    
    public button_button button()
    {
        return this.button;
    }
}
