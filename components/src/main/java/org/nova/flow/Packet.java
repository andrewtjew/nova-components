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
package org.nova.flow;

public class Packet
{
    final static int BEGIN_SEGMENT=-1;
    final static int END_SEGMENT=-2;
    final static int FLUSH=-3;
    
    final private Object[] array;
    private int size;
    public Packet(int capacity)
    {
        if (capacity>=0)
        {
            this.array=new Object[capacity];
            this.size=0;
        }
        else
        {
            this.size=capacity;
            this.array=null;
        }
    }
    Packet(Object[] array)
    {
        this.size=BEGIN_SEGMENT;
        this.array=array;
    }
    
    final static Packet FLUSH_PACKET=new Packet(FLUSH);
    final static Packet END_SEGMENT_PACKET=new Packet(END_SEGMENT);
    
    static Packet BeginSegment(long marker)
    {
        return new Packet(new Object[]{marker});
    }
    
    public boolean add(Object item)
    {
        if (this.size==this.array.length)
        {
            return false;
        }
        this.array[this.size++]=item;
        return true;
    }
    public int sizeOrType()
    {
        return size;
    }
    /*
    public Object[] get()
    {
        return this.array;
    }
    */
    public Object get(int index)
    {
        if (index>=this.size)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.array[index];
    }
    public Object getSegmentObject(int index)
    {
        if (size!=-1)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.array[index];
        
    }
}
