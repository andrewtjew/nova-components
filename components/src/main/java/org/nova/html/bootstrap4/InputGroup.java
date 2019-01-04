package org.nova.html.bootstrap4;

import org.nova.html.elements.Element;

public class InputGroup extends StyleComponent<InputGroup>
{
    public InputGroup()
    {
        super("div","input-group");
    }

    public InputGroup prepend(Element element)
    {
//        returnAddInner(new InputGroupPrepend()).addInner(new InputGroupText().addInner(element));
        returnAddInner(new InputGroupPrepend()).addInner(element);
        return this;
    }

    public InputGroup append(Element element)
    {
        returnAddInner(new InputGroupAppend()).addInner(element);
//        returnAddInner(new InputGroupAppend()).addInner(new InputGroupText().addInner(element));
        return this;
    }

    public InputGroup prepend(String text)
    {
        returnAddInner(new InputGroupPrepend()).returnAddInner(new InputGroupText(text));
        return this;
    }

    public InputGroup append(String text)
    {
        returnAddInner(new InputGroupAppend()).returnAddInner(new InputGroupText(text));
        return this;
    }

    
}
