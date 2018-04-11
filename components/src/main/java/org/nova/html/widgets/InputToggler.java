package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InputElement;
import org.nova.html.tags.input_checkbox;

public class InputToggler extends Element
{
    final private input_checkbox checkbox;
    
    public InputToggler(String name,boolean checked,String id) throws Exception
    {
        if (id==null)
        {
            throw new Exception("id must be set");
        }
        this.checkbox=new input_checkbox().name(name);
        this.checkbox.checked(checked);
        this.checkbox.onclick("document.getElementById('"+id+"').disabled=!this.checked;");
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.checkbox);
    }
}
