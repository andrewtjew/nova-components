package org.nova.lexing;

public class Extra
{
    private final int position;
    private final int length;
    private final Source source;
    private final ExtraNote note;

    public Extra(Source source,ExtraNote note,int position,int length)
    {
        this.source=source;
        this.position=position;
        this.length=length;
        this.note=note;
    }

    public ExtraNote getNote()
    {
        return this.note;
    }
    public String getText()
    {
        return this.source.getText().substring(this.position,this.position+this.length);
    }
    public int getPosition()
    {
        return position;
    }
    public int getLength()
    {
        return length;
    }

    public Source getSource()
    {
        return source;
    }

}
