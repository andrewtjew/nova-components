package org.nova.net.printing;

public class SetNewLineSpacingCommand extends Command
{
    final private float lineSpacing;
    
    public SetNewLineSpacingCommand(float lineSpacing)
    {
        super(Code.SetNewLineSpacing);
        this.lineSpacing=lineSpacing;
    }
    
}
