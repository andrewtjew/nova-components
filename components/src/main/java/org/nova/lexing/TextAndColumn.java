package org.nova.lexing;

public class TextAndColumn
{
    final private String text;
    final private int column;
    public TextAndColumn(String text,int position)
    {
        int startIndex=0;
        for (;;)
        {
            int index=text.indexOf('\n',startIndex);
            if ((index>position)||(index<0))
            {
                break;
            }
            startIndex=index+1;
        }
        int endIndex=text.indexOf('\n',startIndex);
        if (endIndex<0)
        {
            endIndex=text.length();
        }
        this.text=text.substring(startIndex,endIndex);
        this.column=position-startIndex;
    }
    public String getText()
    {
        return text;
    }
    public int getColumn()
    {
        return column;
    }
    
}
