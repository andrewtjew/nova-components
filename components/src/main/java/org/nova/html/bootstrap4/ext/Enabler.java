package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.CustomCheckBox;
import org.nova.html.bootstrap4.CustomControl;
import org.nova.html.bootstrap4.CustomLabel;
import org.nova.html.bootstrap4.ToggleComponent;

/*
public class Enabler extends CustomControl
{
    public Enabler(ToggleComponent<?> targetComponent,boolean addTarget)
    {
        custom_checkbox();
        targetComponent.disabled();
        CustomCheckBox checkbox=returnAddInner(new CustomCheckBox());
        returnAddInner(new CustomLabel()).for_(checkbox).addInner("&#8203;");
        String id=targetComponent.id();
        checkbox.onclick("if (document.getElementById('"+checkbox.id()+"').checked){$('#"+id+"').removeClass('disabled');}else{$('#"+id+"').addClass('disabled');}");
        if (addTarget)
        {
            addInner(targetComponent);
        }
    }
    public Enabler(ToggleComponent<?> targetComponent)
    {
        this(targetComponent,true);
    }
}
*/