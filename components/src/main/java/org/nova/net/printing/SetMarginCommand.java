package org.nova.net.printing;

public class SetMarginCommand extends Command
{ 
    private final Margin margin;
    
    public SetMarginCommand(Margin margin)
    {
        super(Code.SetMargin);
        this.margin=margin;
    }
}
