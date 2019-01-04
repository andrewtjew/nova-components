package org.nova.net.printing;

public class NewLinesCommand extends Command
{
    private int lines;
    public NewLinesCommand(int lines)
    {
        
        super(Code.NewLines);
        this.lines=lines;
    }
    public NewLinesCommand()
    {
        this(0);
    }
    
}
