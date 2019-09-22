package org.nova.net.printing;

public class PrintMachineCodeCommand extends Command
{
    private final String text;
    private final Align align;
    private final Margin margin;
    private final Size thickness;
    private final Size thinness;
    private final Size size;
    private final MachineCode machineCode;
    
    
    public PrintMachineCodeCommand(MachineCode machineCode,Object text,Align align,Margin margin,Size thinness,Size thickness,Size size)
    {
        super(Code.PrintMachineCode);
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
        this.thickness=thickness;
        this.thinness=thinness;
        this.size=size;
        this.machineCode=machineCode;
    }
    public PrintMachineCodeCommand(Object text,Align align)
    {
        this(MachineCode.Code_25_Interleaved_2_Of_5,text, align, null,new Size(4,Unit.Point),new Size(12,Unit.Point),new Size(100,Unit.Point));
    }
    
}
