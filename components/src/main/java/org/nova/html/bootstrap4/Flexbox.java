package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.Flex;
import org.nova.html.bootstrap4.classes.Justify;

public class Flexbox extends Component<Flexbox> 
{
    public Flexbox(Flex flex)
    {
        super("div",flex.toString());
    }
    
    public Flexbox align_items(AlignSelf value)
    {
        return addClass("align-items",value);
    }
    
    public Flexbox justify_content(Justify value)
    {
        return addClass("justify-content",value);
    }
    
    
    public Flexbox wrap()
    {
        return addClass("flex-wrap");
    }

    public Flexbox wrap_reverse()
    {
        return addClass("flex-wrap-reverse");
    }
}
