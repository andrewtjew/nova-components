package org.nova.net.printing;

public class PrintPageCounterCommand extends Command
{
    private final String before;
    private final String after;
    private final Align align;
    private final Margin margin;
    
    public PrintPageCounterCommand(Object before,Object after,Align align,Margin margin)
    {
        super(Code.PrintPageCounter);
        if (before==null)
        {
            this.before=null;
        }
        else
        {
            this.before=before.toString();
        }
        if (after==null)
        {
            this.after=null;
        }
        else
        {
            this.after=after.toString();
        }
        this.align=align;
        this.margin=margin;
    }
    public PrintPageCounterCommand(Object before,Object after,Align align)
    {
        this(before,after, align, null);
    }
    
}
