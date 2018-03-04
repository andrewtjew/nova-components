package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class ButtonGroup extends Element
{
    final private div div;
    private ButtonSize buttonSize;
    
    public ButtonGroup(String label)
    {
        this.div=new div();
    }
    
    public ButtonGroup buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("btn-group");
        cb.add("btn-group",buttonSize);
        cb.applyTo(this.div);
        composer.render(this.div);
    }
    
}
