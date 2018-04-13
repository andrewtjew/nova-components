package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InputElement;
import org.nova.html.tags.div;
import org.nova.html.tags.form;
import org.nova.html.tags.i;
import org.nova.html.tags.input_password;
import org.nova.html.tags.input_text;
import org.nova.html.tags.label;
import org.nova.html.tags.span;
import org.nova.html.tags.textarea;

public class Form extends Element
{
    final private form form;
    
    public Form(String action)
    {
        this.form=new form().action(action);
    }
    
    public Form add(String label,InputElement<?> input)
    {
        div div=this.form.returnAddInner(new div()).class_("form-group");
        div.returnAddInner(new label()).for_(input.id()).addInner(label);
        input.class_("form-control");
        div.addInner(input);
        return this;
    }
    
    public Form add(String label,textarea input)
    {
        div div=this.form.returnAddInner(new div()).class_("form-group");
        div.returnAddInner(new label()).for_(input.id()).addInner(label);
        input.class_("form-control");
        div.addInner(input);
        return this;
    }
    
    public Form add(Element element)
    {
        div div=this.form.returnAddInner(new div()).class_("form-group");
        div.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.form);
    }
    
}
