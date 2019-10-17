package org.nova.html.deprecated;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.input_checkbox;

public class DisableElementToggler extends Element
{
    final private input_checkbox checkbox;
    
    public DisableElementToggler(String name,boolean checked,String targetId,String id) throws Exception
    {
        if (targetId==null)
        {
            throw new Exception("targetId is null");
        }
        this.checkbox=new input_checkbox();
        if (name!=null)
        {
            this.checkbox.name(name);
        }
        if (id!=null)
        {
            this.checkbox.id(id);
        }
        this.checkbox.checked(checked);
        this.checkbox.onclick("document.getElementById('"+targetId+"').disabled=this.checked;");
    }

    public DisableElementToggler(String name,boolean checked,String targetId) throws Exception
    {
        this(name,checked,targetId,null);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.checkbox);
    }
}
