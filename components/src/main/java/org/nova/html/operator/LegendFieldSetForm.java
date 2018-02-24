package org.nova.html.operator;

import org.nova.html.elements.InnerElement;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.tags.fieldset;
import org.nova.html.tags.form_get;
import org.nova.html.tags.form_post;
import org.nova.html.tags.legend;

public class LegendFieldSetForm extends InnerElement<LegendFieldSetForm>
{
    final private GlobalEventTagElement<?> form;
    final private fieldset fieldset;
    final private legend legend;
    
    public LegendFieldSetForm(String heading,String actionURL,boolean post)
    {
        if (post)
        {
            this.form=new form_post().action(actionURL);
        }
        else
        {
            this.form=new form_get().action(actionURL);
        }
        super.addInner(this.form);
        this.fieldset=this.form.returnAddInner(new fieldset());
        this.legend=this.fieldset.returnAddInner(new legend()).addInner(heading);
    }
    public LegendFieldSetForm(String heading,String actionURL)
    {
        this(heading,actionURL,true);
    }
    
    @Override
    public LegendFieldSetForm addInner(Element element)
    {
        this.fieldset.addInner(element);
        return this;
    }
    @Override
    public LegendFieldSetForm setInner(Element element)
    {
        this.fieldset.setInner(element);
        return this;
    }
    @Override
    public LegendFieldSetForm addInners(Element... elements)
    {
        this.fieldset.addInners(elements);
        return this;
    }
    @Override
    public LegendFieldSetForm addInner(Object object)
    {
        this.fieldset.addInner(object);
        return this;
    }
    
    
    
}
