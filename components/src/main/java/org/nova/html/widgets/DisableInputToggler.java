package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InputElement;
import org.nova.html.tags.input_checkbox;

public class DisableInputToggler extends Element
{
    final private input_checkbox checkbox;
    
    public DisableInputToggler(String name,boolean checked,String targetId,String id) throws Exception
    {
        if (targetId==null)
        {
            throw new Exception("targetId is null");
        }
        this.checkbox=new input_checkbox().name(name);
        if (id!=null)
        {
            this.checkbox.id(id);
        }
        this.checkbox.checked(checked);
        this.checkbox.onclick("document.getElementById('"+targetId+"').disabled=this.checked;");
    }

    public DisableInputToggler(String name,boolean checked,String targetId) throws Exception
    {
        this(name,checked,targetId,null);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.checkbox);
    }
}
