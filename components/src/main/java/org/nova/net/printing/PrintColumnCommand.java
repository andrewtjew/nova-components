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
