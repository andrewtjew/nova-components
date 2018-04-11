package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;
import org.nova.html.bootstrap4.classes.DropdownPosition;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class ButtonGroup extends Element
{
    final private div div;
    private ButtonSize buttonSize;
    private DropdownPosition dropdownPosition;
    
    public ButtonGroup(String label)
    {
        this.div=new div();
    }
    
    public ButtonGroup buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    
    public ButtonGroup dropdownPosition(DropdownPosition value)
    {
        this.dropdownPosition=value;
        return this;
    }
    
    public ButtonGroup add(ButtonDropdown button)
    {
        this.div.addInner(button);
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("btn-group");
        cb.addIf(this.buttonSize!=null,"btn-group",this.buttonSize);
        cb.addIf(this.dropdownPosition!=null,"drop"+this.dropdownPosition);
        cb.applyTo(this.div);
        composer.render(this.div);
    }
    
}
