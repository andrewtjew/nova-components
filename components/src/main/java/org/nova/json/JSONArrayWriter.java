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
