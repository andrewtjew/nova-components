package org.nova.net.printing;

public class BeginFooterCommand extends Command
{
    final public Size height;
    public BeginFooterCommand(Size height)
    {
        super(Code.BeginFooter);
        this.height=height;
    }
    
}
