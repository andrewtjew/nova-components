package org.nova.json;

public class JSONArrayWriter extends JSONWriter
{
    public JSONArrayWriter()
    {
        this(new StringBuilder(),null);
    }
    
    JSONArrayWriter(StringBuilder sb,JSONWriter parent)
    {
        super(sb,parent);
        sb.append('[');
    }
    
    public JSONArrayWriter writeNull() throws Exception
    {
        prepare();
        sb.append("null");
        return this;
    }
    
    public JSONArrayWriter write(long value) throws Exception
    {
        prepare();
        sb.append(value);
        return this;
    }
    public JSONArrayWriter write(int value) throws Exception
    {
        prepare();
        sb.append(value);
        return this;
    }
    public JSONArrayWriter write(Long value) throws Exception
    {
        if (value==null)
        {
            return writeNull();
        }
        prepare();
        sb.append(value);
        return this;
    }

    public JSONArrayWriter write(String value) throws Throwable
    {
        if (value==null)
        {
            return writeNull();
        }
        prepare();
        sb.append('"');
        sb.append(ObjectMapper.writeObjectToString(value));
        sb.append('"');
        return this;
    }
    
    public JSONObjectWriter openObjectWriter() throws Exception
    {
        prepare();
        open();
        return new JSONObjectWriter(this.sb,this);
    }
    
    
    public JSONArrayWriter openArrayWriter() throws Exception
    {
        prepare();
        open();
        return new JSONArrayWriter(this.sb,this);
    }

    @Override
    protected void writeClose()
    {
        sb.append(']');
    }

}
