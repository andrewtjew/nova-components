package org.nova.collections;

public class CharArrayBox
{
    private char[] array;
    public CharArrayBox(int length)
    {
        this.array=new char[length];
    }
    
    public char[] get()
    {
        return this.array;
    }
    public char get(int index)
    {
        return this.array[index];
    }
    public void set(int index,char c)
    {
        this.array[index]=c;
    }
    
    public void grow(int minimumSize)
    {
        if (minimumSize>this.array.length)
        {
            char[] expanded=new char[minimumSize];
            System.arraycopy(this.array, 0, expanded, 0, this.array.length);
            this.array=expanded;
        }
    }
    
    public void grow()
    {
        grow(this.array.length*2);
    }
    
}
