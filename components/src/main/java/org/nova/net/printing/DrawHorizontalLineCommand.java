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
