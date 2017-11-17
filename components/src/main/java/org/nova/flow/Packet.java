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
