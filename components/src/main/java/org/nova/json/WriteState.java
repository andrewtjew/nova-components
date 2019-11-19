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
package org.nova.json;

import java.io.PrintStream;
import java.util.ArrayList;

public class WriteState
{
    final private String indent;
    final private String endOfLine;
    final private ArrayList<String> levelIndents;
    final private PrintStream stream;
    private int level;
    private String currentIndent;
    
    public WriteState(String indent,String endOfLine,PrintStream stream)
    {
        this.indent=indent;
        this.endOfLine=endOfLine;
        this.levelIndents=new ArrayList<>();
        this.levelIndents.add(null);
        this.stream=stream;
    }
    public void begin(char character)
    {
        this.stream.print(character);
        if (this.endOfLine!=null)
        {
            this.level++;
            if (this.level<this.levelIndents.size())
            {
                this.currentIndent=this.levelIndents.get(this.level-1);
                return;
            }
            if (this.level==1)
            {
                this.currentIndent=this.indent;
            }
            else
            {
                this.currentIndent=this.currentIndent+this.indent;
            }
            this.levelIndents.add(this.currentIndent);
        }
    }
    public void end(char character)
    {
        if (this.endOfLine!=null)
        {
            this.stream.print(this.endOfLine);
            this.level--;
            this.currentIndent=this.levelIndents.get(this.level);
            if (this.currentIndent!=null)
            {
                this.stream.print(this.currentIndent);
            }
        }
        this.stream.print(character);
    }
    
    public void writeName(char[] characters)
    {
        this.stream.print(characters);
    }
    public void writeSeperator(boolean needComma)
    {
        if (needComma)
        {
           this.stream.print(',');
        }
        if (this.endOfLine!=null)
        {
            this.stream.print(endOfLine);
            if (this.currentIndent!=null)
            {
                this.stream.print(this.currentIndent);
            }
        }
    }
    public void write(String object)
    {
        this.stream.print(object);
    }
    public void writeQuoted(String object)
    {
        this.stream.print('"');
        this.stream.print(object);
        this.stream.print('"');
    }
    public void writeNull()
    {
        this.stream.print("null");
    }
    public void writeEscapedString(String string)
    {
        this.stream.print('"');
        for (int index=0;index<string.length();index++)
        {
            char c=string.charAt(index);
            if (c=='\\')
            {
                this.stream.print(c);
                this.stream.print(c);
            }
            else if (c>'"')
            { 
                this.stream.print(c);
            }
            else if (c=='"')
            {
                this.stream.print('\\');
                this.stream.print('"');
            }
            else if (c=='\b')
            {
                this.stream.print('\\');
                this.stream.print('b');
            }
            else if (c=='\f')
            {
                this.stream.print('\\');
                this.stream.print('f');
            }
            else if (c=='\n')
            {
                this.stream.print('\\');
                this.stream.print('n');
            }
            else if (c=='\r')
            {
                this.stream.print('\\');
                this.stream.print('r');
            }
            else if (c=='\t')
            {
                this.stream.print('\\');
                this.stream.print('t');
            }
            else
            {
                this.stream.print(c);
            }
        }
        this.stream.print('"');
    }
    
}
