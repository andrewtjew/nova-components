package org.nova.net.printing;

public class PrintLineCommand extends Command
{
    private final String text;
    private final Align align;
    private final Margin margin;
    
    public PrintLineCommand(Object text,Align align,Margin margin)
    {
        super(Code.PrintLine);
        if (text==null)
        {
            this.text=null;
        }
        else
        {
            this.text=text.toString();
        }
        this.align=align;
        this.margin=margin;
    }
    public PrintLineCommand(Object text,Align align)
    {
        this(text, align, null);
    }
    
}
