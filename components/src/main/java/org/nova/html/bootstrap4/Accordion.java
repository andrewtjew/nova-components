package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.TextAlign;
import org.nova.html.elements.Composer;
import org.nova.html.ext.Content;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.script;
import org.nova.html.tags.span;
import org.nova.html.tags.ul;

public class Accordion extends StyleComponent<Accordion>
{
    public Accordion()
    {
        super("div","accordion");
    }
    
    public Accordion add(String title,String text)
    {
        Card card=returnAddInner(new Card());
        Button button=card.returnAddInner(new Button()).addInner(title);
        button.rounded(0);
        button.text(TextAlign.left);
        button.addClass("btn-accordion");
        
        Collapse collapse=returnAddInner(new Collapse());
        collapse.linkDataParent(card);
        CardBody body=collapse.returnAddInner(new CardBody());
        body.border();
        body.addInner(text);
        
        button.toggleCollapse(collapse);
        
        return this;
    }
    
    
}
