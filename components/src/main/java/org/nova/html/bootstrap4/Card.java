package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Size;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.widgets.Content;

public class Card extends Element 
{
    final private div div;
    private div header;
    private div footer;
    private div body;
    
    private Size size;
    
	public Card()
	{
	    this.div=new div();
	}
    public Card addToHeader(Element element)
    {
        if (this.header==null)
        {
            this.header=new div().class_("card-header");
        }
        this.header.addInner(element);
        return this;
    }
    public Card add(Element element)
    {
        if (this.body==null)
        {
            this.body=new div().class_("card-body");
        }
        this.body.addInner(element);
        return this;
    }
    public Card addToFooter(Element element)
    {
        if (this.footer==null)
        {
            this.footer=new div().class_("card-footer");
        }
        this.footer.addInner(element);
        return this;
    }
    public Card size(Size value)
    {
        this.size=value;
        return this;
    }
    
	@Override
    public void compose(Composer composer) throws Throwable
    {
	    ClassBuilder cb=new ClassBuilder("card");
//	    cb.addIf(this.size!=null,"well",this.size);
	    cb.applyTo(this.div);
	    
        this.div.addInner(this.header);
        this.div.addInner(this.body);
        this.div.addInner(this.footer);
	    
        composer.render(this.div);
    }
	
}
