package org.nova.net.printing;

public class DrawHorizontalLineCommand extends Command
{ 
    private final Align align;
    private final Size length;
    private final Size thickness;
    private final Margin margin;
    private final Color color;
    
    public DrawHorizontalLineCommand(Size length,Size thickness,Align align,Margin margin,Color color)
    {
        super(Code.DrawHorizontalLine);
        this.length=length;
        this.thickness=thickness;
        this.align=align;
        this.margin=margin;
        this.color=color;
    }
    public DrawHorizontalLineCommand(Size length,Size thickness,Align align,Margin margin)
    {
        this(length,thickness,align,margin,null);
    }
    public DrawHorizontalLineCommand(Size length,Size thickness)
    {
        this(length,thickness,Align.Center,null);
    }

    public DrawHorizontalLineCommand(Size thickness)
    {
        this(new Size(100,Unit.Percent),thickness);
    }
    public DrawHorizontalLineCommand(Size thickness,Margin margin)
    {
        this(new Size(100,Unit.Percent),thickness,Align.Center,margin);
    }
    public DrawHorizontalLineCommand(Size thickness,Margin margin,Color color)
    {
        this(new Size(100,Unit.Percent),thickness,Align.Center,margin,color);
    }
}
