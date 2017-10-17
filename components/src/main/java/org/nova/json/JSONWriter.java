package org.nova.json;

public abstract class JSONWriter implements AutoCloseable
{
    StringBuilder sb;
    private boolean writable;
    private boolean commaNeeded;
    private JSONWriter parent;
    
    JSONWriter(StringBuilder sb,JSONWriter parent)
    {
        this.sb=sb;
        this.commaNeeded=false;
        this.writable=true;
        this.parent=parent;
    }

    void open()
    {
        this.writable=false;
    }
    public JSONObjectWriter openObject(String name) throws Exception
    {
        prepare();
        sb.append('"').append(name).append('"').append(':');
        sb.append('"');
        open();
        return new JSONObjectWriter(this.sb,this);
    }
    
    
    void prepare() throws Exception
    {
        if (this.writable==false)
        {
            if (this.sb==null)
            {
                throw new Exception("Writer is closed.");
            }
            throw new Exception("Cannot write. An inner writer is active and must be closed first");
        }
        if (this.commaNeeded)
        {
            sb.append(',');
        }
        else
        {
            this.commaNeeded=true;
        }
    }

    public String getText() throws Exception
    {
        if (this.parent!=null)
        {
            throw new Exception("Cannot be called on an inner writer.");
        }
        if (this.writable)
        {
            close();
        }
        return sb.toString();
    }
    
    protected abstract void writeClose();
    

    @Override
    public void close() throws Exception
    {
        if (this.writable)
        {
            writeClose();
        }
        this.writable=false;
        if (this.parent!=null)
        {
            this.parent.writable=true;
            this.sb=null;
        }
    }
}
