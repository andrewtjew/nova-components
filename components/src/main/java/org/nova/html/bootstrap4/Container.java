package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;

public class Container extends StyleComponent<Container>
{
    private boolean fluid;
    
	public Container()
	{
	    super("div",null);
	}
    public Container fluid()
    {
        this.fluid=true;
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (fluid)
        {
            this.addClass("container","fluid");
        }
        else
        {
            this.addClass("container");
        }
        super.compose(composer);
    }
}
