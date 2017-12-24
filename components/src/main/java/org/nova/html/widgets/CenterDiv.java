package org.nova.html.widgets;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.properties.Size;
import org.nova.html.properties.Style;
import org.nova.html.properties.position;
import org.nova.html.properties.unit;
import org.nova.html.tags.div;

public class CenterDiv extends GlobalEventTagElement<CenterDiv>
{
    public CenterDiv(Size width)
    {
        super("div");
        style(new Style().position(position.relative).left(new Size(50,unit.percent)).width(width).margin_left(new Size(-width.value()/2,width.unit())));
    }
    
    @Override
    public CenterDiv addInner(Element element)
    {
        super.addInner(element);
        return this;
    }

    @Override
    public CenterDiv setInner(Element element)
    {
        super.setInner(element);
        return this;
    }

    @Override
    public CenterDiv addInners(Element... elements)
    {
        super.addInners(elements);
        return this;
    }

    @Override
    public CenterDiv addInner(Object object)
    {
        super.addInner(object);
        return this;
    }

}
