package org.nova.net.printing;

public class DrawURLImageCommand extends Command
{ 
    private final String URL;
    private final Size width;
    private final Size height;
    private final Margin margin;
    private final Border border;
    private final Padding padding;
    private final Align align;
    
    public DrawURLImageCommand(String URL,Align align,Size width,Size height,Margin margin,Border border,Padding padding)
    {
        super(Code.DrawURLImage);
        this.URL=URL;
        this.width=width;
        this.height=height;
        this.margin=margin;
        this.padding=padding;
        this.border=border;
        this.align=align;
    }
    public DrawURLImageCommand(String URL,Align align)
    {
        this(URL,align,null,null,null,null,null);
    }
}
