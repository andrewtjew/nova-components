package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InputElement;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.form;
import org.nova.html.tags.input_email;
import org.nova.html.tags.label;

public class Form extends Element
{
    final private form form;
    
    public Form(String action)
    {
        this.form=new form().action(action);
    }
    
    public Form add(String label,String forId,InputElement<?> element)
    {
        div div=this.form.returnAddInner(new div()).class_("form-group");
        div.returnAddInner(new label()).for_(forId).addInner(label);
        div.addInner(element);
//        org.apache.commons.lang.m
        return this;
    }
    
    public Form add(Element element)
    {
        this.form.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.form);
    }
    
}
