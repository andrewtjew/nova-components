package org.nova.html.bootstrap4;


public class ButtonGroup extends Component<ButtonGroup>
{
    public ButtonGroup()
    { 
        super("div","btn-group");
        attr("type","button");
    }
    
    public ButtonGroup toggleButtons()
    {
        attr("data-toggle","buttons");
        addClass("btn-group-toggle");
        return this;
    }
    public ButtonGroup vertical()
    {
        addClass("btn-group-vertical");
        return this;
    }
    
}
