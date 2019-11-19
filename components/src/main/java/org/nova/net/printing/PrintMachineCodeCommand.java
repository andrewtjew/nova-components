/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
