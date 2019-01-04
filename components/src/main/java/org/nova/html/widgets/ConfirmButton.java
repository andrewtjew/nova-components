package org.nova.html.widgets;

import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.input_checkbox;

public class ConfirmButton extends Element
{
    final private div content;

    public ConfirmButton(String location,String label,String title)
    {
        input_checkbox check=new input_checkbox();
        check.id();
        button_button button=new button_button();
        button.addInner(label);
        if (title!=null)
        {
            button.title(title);
        }
        button.onclick("if (document.getElementById('"+check.id()+"').checked==true) {window.location='"+location+"';}");
        this.content=new div().addInner(check).addInner(button);

    }

    public ConfirmButton(String location,String label)
    {
        this(location,label,null);
    }
    public ConfirmButton style(String style)
    {
        this.content.style(style);
        return this;
    }

    public ConfirmButton style(Style style)
    {
        this.content.style(style);
        return this;
    }

    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.content);
    }
}
