package org.nova.html.bootstrap4;

public class Row extends StyleComponent<Row> 
{
    public Row()
	{
        super("div","row");
	}

    public Row no_gutters()
    {
        addClass("no-gutters");
        return this;
    }
    
}
