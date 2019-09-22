package org.nova.net.printing;

public class MoveCursorCommand extends Command
{ 
    private final Size x;
    private final Size y;
    
    public MoveCursorCommand(Size x,Size y)
    {
        super(Code.SetCursor);
        this.x=x;
        this.y=y;
    }
}
