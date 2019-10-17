package org.nova.html.deprecated;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.form_post;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_submit;

public class CheckActionPostForm extends Element
{
    final private form_post form;
    public CheckActionPostForm(String action,String checkName,String buttonLabel)
    {
        this.form=new form_post().action(action);
        this.form.returnAddInner(new input_checkbox().name(checkName));
        this.form.returnAddInner(new input_submit().value(buttonLabel));
    }
    
    public CheckActionPostForm add(Element element)
    {
        this.form.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.form);
    }
}
