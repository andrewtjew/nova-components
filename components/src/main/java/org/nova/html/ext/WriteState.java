package org.nova.html.ext;

import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.commons.lang.text.StrBuilder;
import org.nova.html.elements.QuotationMark;

public class WriteState
{
    final private StringBuilder sb;
    final private QuotationMark mark;
    public WriteState(QuotationMark mark)
    {
        this.sb=new StringBuilder();
        this.mark=mark;
    }
    public void begin(char character)
    {
        this.sb.append(character);
    }
    public void end(char character)
    {
        this.sb.append(character);
    }
    
    public void writeName(String name)
    {
        this.sb.append(name);
    }
    public void writeSeperator(boolean needComma)
    {
        if (needComma)
        {
            this.sb.append(',');
        }
    }
    public void write(String object)
    {
        this.sb.append(object);
    }
    public void writeQuoted(String object)
    {
        this.sb.append(this.mark);
        this.sb.append(object);
        this.sb.append(this.mark);
    }
    public void writeNull()
    {
        this.sb.append("null");
    }
    public void writeEscapedString(String string)
    {
        this.sb.append(this.mark);
        for (int index=0;index<string.length();index++)
        {
            char c=string.charAt(index);
            if (c=='\\')
            {
                this.sb.append(c);
                this.sb.append(c);
            }
            else if (c>'"')
            { 
                this.sb.append(c);
            }
            else if (c=='"')
            {
                this.sb.append('\\');
                this.sb.append('"');
            }
            else if (c=='\b')
            {
                this.sb.append('\\');
                this.sb.append('b');
            }
            else if (c=='\f')
            {
                this.sb.append('\\');
                this.sb.append('f');
            }
            else if (c=='\n')
            {
                this.sb.append('\\');
                this.sb.append('n');
            }
            else if (c=='\r')
            {
                this.sb.append('\\');
                this.sb.append('r');
            }
            else if (c=='\t')
            {
                this.sb.append('\\');
                this.sb.append('t');
            }
            else
            {
                this.sb.append(c);
            }
        }
        this.sb.append(this.mark);
    }
    
    @Override
    public String toString()
    {
        return this.sb.toString();
    }
}
