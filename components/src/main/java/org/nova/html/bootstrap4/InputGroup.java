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

public class InputGroup extends Element
{
    final private div div;
    final private Element input;
    private div prepend;
    private div append;
    
    public InputGroup(InputElement<?> input)
    {
        input.class_("form-control");
        this.input=input;
        this.div=new div().class_("input-group");
    }

    public InputGroup(Element input)
    {
        this.input=input;
        this.div=new div().class_("input-group");
    }
    
    public InputGroup prepend(String label)
    {
        this.prepend=new div().class_("input-group-prepend").addInner(new span().class_("input-group-text").addInner(label));
        return this;
    }
    public InputGroup prepend(Element element)
    {
        this.prepend=new div().class_("input-group-append").addInner(new span().class_("input-group-text").addInner(element));
        return this;
    }

    public InputGroup append(String label)
    {
        this.append=new div().class_("input-group-append").addInner(new span().class_("input-group-text").addInner(label));
        return this;
    }
    public InputGroup append(Element element)
    {
        this.append=new div().class_("input-group-append").addInner(new span().class_("input-group-text").addInner(element));
        return this;
    }

    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.div.addInner(this.prepend);
        this.div.addInner(this.input);
        this.div.addInner(this.append);
        composer.render(this.div);
    }
    
}
