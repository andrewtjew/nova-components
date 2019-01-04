package org.nova.net.printing;

public class PrintColumnCommand extends Command
{
    private final String text;
    private final Justify justify;
    private final Margin margin;
    private final Size width;
    
    public PrintColumnCommand(Object text,Size width,Justify justify,Margin margin)
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
        this.width=width;
        this.margin=margin;
        this.justify=justify;
    }

    public PrintColumnCommand(Object text,Size width)
    {
        this(text, width,Justify.Center, null);
    }

}
