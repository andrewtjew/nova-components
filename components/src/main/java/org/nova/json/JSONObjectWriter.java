package org.nova.json;

public class JSONObjectWriter extends JSONWriter
{
    public JSONObjectWriter()
    {
        this(new StringBuilder(),null);
    }
    
    JSONObjectWriter(StringBuilder sb,JSONWriter parent)
    {
        super(sb,parent);
        sb.append('{');
    }
    
    public JSONObjectWriter writeNull(String name) throws Exception
    {
        prepare();
        sb.append('"').append(name).append("\":null");
        return this;
    }
    
    public JSONObjectWriter write(String name,long value) throws Exception
    {
        prepare();
        sb.append('"').append(name).append('"').append(':').append(value);
        return this;
    }
    public JSONObjectWriter write(String name,int value) throws Exception
    {
        prepare();
        sb.append('"').append(name).append('"').append(':').append(value);
        return this;
    }
    public JSONObjectWriter write(String name,Long value) throws Exception
    {
        if (value==null)
        {
            return writeNull(name);
        }
        prepare();
        sb.append('"').append(name).append('"').append(':').append(value);
        return this;
    }

    public JSONObjectWriter write(String name,String value) throws Exception
    {
        if (value==null)
        {
            return writeNull(name);
        }
        prepare();
        sb.append('"').append(name).append('"').append(':').append('"');
        ObjectMapper.writeString(this.sb, value);
        sb.append('"');
        return this;
    }
    
    public JSONObjectWriter openObjectWriter(String name) throws Exception
    {
        prepare();
        sb.append('"').append(name).append('"').append(':');
        sb.append('"');
        open();
        return new JSONObjectWriter(this.sb,this);
    }
    
    public JSONArrayWriter openArrayWriter(String name) throws Exception
    {
        prepare();
        sb.append('"').append(name).append('"').append(':');
        open();
        return new JSONArrayWriter(this.sb,this);
    }
    

    @Override
    protected void writeClose()
    {
        sb.append('}');
    }
}
