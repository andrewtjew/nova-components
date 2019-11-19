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
