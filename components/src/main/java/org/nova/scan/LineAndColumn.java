package org.nova.scan;

public class LineAndColumn
{
    final private int line;
    final private int column;
    public LineAndColumn(String text,int position)
    {
        int line=0;
        int startIndex=0;
        for (;;)
        {
            int index=text.indexOf('\n',startIndex);
            if ((index>position)||(index<0))
            {
                break;
            }
            line++;
            startIndex=index+1;
        }
        this.line=line;
        this.column=position-startIndex;
     }
    public int getLine()
    {
        return line;
    }
    public int getColumn()
    {
        return column;
    }
    
}
