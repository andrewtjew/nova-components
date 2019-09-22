package org.nova.net.printing;

public class PrintColumnCommand extends Command
{
    private final String text;
    private final Justify justify;
    private final Size width;
    private final Margin margin;
    private final Padding padding;
    private final Border border;
    
    public PrintColumnCommand(Object text,Size width,Justify justify,Margin margin,Border border,Padding padding)
    {
        super(Code.PrintColumn);
        if (text==null)
        {
            this.text=null;
        }
        else
        {
            this.text=text.toString();
        }
        if (justify==null)
        {
            justify=Justify.Center;
        }
        this.width=width;
        this.margin=margin;
        this.justify=justify;
        this.border=border;
        this.padding=padding;
    }
    public PrintColumnCommand(Object text,Size width)
    {
        this(text, width,Justify.Center, null,null,null);
    }
    public PrintColumnCommand(Object text,Size width,Justify justify)
    {
        this(text, width,justify,null,null,null);
    }

}
