package org.nova.html.bootstrap4;


public class Button extends ButtonComponent<Button>
{
    public Button()
    { 
        super("button");
        attr("type","button");
    }
    public Button(String label)
    { 
        super("button");
        attr("type","button");
        addInner(label);
    }

}
