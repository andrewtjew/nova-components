package org.nova.html.bootstrap4;


public class Toolbar extends Component<Toolbar>
{
    public Toolbar()
    { 
        super("div","btn-group");
        attr("type","button");
    }
    
    public Toolbar toggleButtons()
    {
        attr("data-toggle","buttons");
        addClass("btn-group-toggle");
        return this;
    }
    public Toolbar vertical()
    {
        addClass("btn-group-vertical");
        return this;
    }
    
}
