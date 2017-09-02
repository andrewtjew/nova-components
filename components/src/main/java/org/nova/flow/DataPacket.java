package org.nova.flow;

public class DataPacket
{
    final static int BEGIN_SEGMENT=-1;
    final static int END_SEGMENT=-2;
    final static int FLUSH=-3;
    
    final private Object[] array;
    private int size;
    public DataPacket(int capacity)
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
    DataPacket(Object[] array)
    {
        this.size=BEGIN_SEGMENT;
        this.array=array;
    }
    
    final static DataPacket FLUSH_PACKET=new DataPacket(FLUSH);
    final static DataPacket END_SEGMENT_PACKET=new DataPacket(END_SEGMENT);
    
    static DataPacket BeginSegment(long marker)
    {
        return new DataPacket(new Object[]{marker});
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
    public int size()
    {
        return size;
    }
    public Object[] get()
    {
        return this.array;
    }
    public Object get(int index)
    {
        if (index>=this.size)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.array[index];
    }
}
