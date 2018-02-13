package org.nova.parsing.section;

import java.util.List;

import org.nova.lexing.Lexeme;

public class Section
{
    final int start;
    final int end;
    Section(int start,int end)
    {
        this.start=start;
        this.end=end;
    }
    public int getStart()
    {
        return start;
    }
    public int getEnd()
    {
        return end;
    }
    public int size()
    {
        return end-start;
    }
}
