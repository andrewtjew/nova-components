package org.nova.net.printing;

public class PrintParagraphCommand extends Command
{
    private final String text;
    private final Justify justify;
    
    public PrintParagraphCommand(Object text,Justify justify)
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
        this.justify=justify;
    }

    public PrintParagraphCommand(Object text)
    {
        this(text, Justify.Full);
    }

}
