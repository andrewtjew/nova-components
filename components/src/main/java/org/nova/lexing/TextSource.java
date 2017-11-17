package org.nova.lexing;

public class TextSource extends Source
{
    static public class TextSnippet extends Snippet
    {
        final private String text;
        final private int start;
        final private int end;

        public TextSnippet(String text,int start,int end)
        {
            this.text=text;
            this.start=start;
            this.end=end;
        }
        
        @Override
        public String getTarget()
        {
            return this.text.substring(this.start,this.end);
        }
        @Override
        public String getBuffer()
        {
            return this.text;
        }
        @Override
        public int getTargetBufferPosition()
        {
            return this.start;
        }
        @Override
        public int getTargetAbsolutePosition()
        {
            return this.start;
        } 
    }

    private final String text;
    private final int length;
    private int index;
    private int beginIndex;
    
    public TextSource(String text)
    {
        this.text=text;
        this.length=text.length();
        this.index=0;
    }

    @Override
    public char next()
    {
        if (this.index>=this.length)
        {
            if (this.index==this.length)
            {
                this.index++;
            }
            return 0;
        }
        return this.text.charAt(this.index++);
    }

    @Override
    public void begin(int revert)
    {
        this.beginIndex=this.index-revert;
    }

    @Override
    public void end(int revert)
    {
        this.index=this.index-revert;
    }

    @Override
    public TextSnippet endAndGetSnippet(int revert)
    {
        end(revert);
        return new TextSnippet(this.text, this.beginIndex, this.index);
    }

    @Override
    public String endContext()
    {
        return this.text;
    }

    @Override
    public void beginContext()
    {
    }

    @Override
    public int getContextPosition()
    {
        return this.index;
    }

    @Override
    public void revert()
    {
        this.index=this.beginIndex;
    }


}
