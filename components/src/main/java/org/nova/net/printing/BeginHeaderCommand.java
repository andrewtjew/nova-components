package org.nova.net.printing;

public class BeginHeaderCommand extends Command
{
    final public Size height;
    public BeginHeaderCommand(Size height)
    {
        super(Code.BeginHeader);
        this.height=height;
    }
    
}
