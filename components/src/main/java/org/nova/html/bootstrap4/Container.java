package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.widgets.Content;

public class Container extends Element 
{
    boolean fluid;
    final private Content content;
    
	public Container()
	{
	    this.content=new Content();
	}
    public Container fluid(boolean fluid)
    {
        this.fluid=fluid;
        return this;
    }
	@Override
    public void compose(Composer composer) throws Throwable
    {
	    div div=new div();
	    ClassBuilder class_=this.fluid?new ClassBuilder("container","fluid"):new ClassBuilder("container");
	    class_.applyTo(div);
	    div.addInner(this.content);
        composer.render(div);
    }
	
	public Container add(Row row)
	{
	    this.content.addInner(row);
	    return this;
	}
}
