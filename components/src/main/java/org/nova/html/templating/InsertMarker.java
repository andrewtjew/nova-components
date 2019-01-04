package org.nova.html.templating;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

public class InsertMarker extends Element
{
    final public String name;
    
    public InsertMarker(String name)
    {
        this.name=name;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (composer instanceof TemplateComposer)
        {
            TemplateComposer templateComposer=(TemplateComposer)composer;
            templateComposer.mark(this);
        }
    }

}
