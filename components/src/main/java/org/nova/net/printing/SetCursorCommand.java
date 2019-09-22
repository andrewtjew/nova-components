package org.nova.net.printing;

public class SetCursorCommand extends Command
{ 
    private final Size x;
    private final Size y;
    
    public SetCursorCommand(Size x,Size y)
    {
        super(Code.SetCursor);
        this.x=x;
        this.y=y;
    }
}
