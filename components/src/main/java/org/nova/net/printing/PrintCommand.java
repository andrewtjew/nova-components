package org.nova.net.printing;

public class PrintCommand extends Command
{
    private final String text;
    private final Align align;
    private final Margin margin;
    
    public PrintCommand(Object text,Align align,Margin margin)
    {
        super(Code.Print);
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
    public PrintCommand(Object text,Align align)
    {
        this(text, align, null);
    }
    public PrintCommand(Object text)
    {
        this(text, null, null);
    }
    
}
