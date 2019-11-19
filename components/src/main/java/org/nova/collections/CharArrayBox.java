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
