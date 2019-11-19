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

public class Tapper extends Node
{
    final private Node receiver;
    private Node tap;
    
    
    public Tapper(Node receiver,Node tap)
    {
        this.receiver=receiver;
        this.tap=tap;
    }
    public Tapper(Node receiver)
    {
        this(receiver,null);
    }
    
    public Node getReceiver()
    {
        return this.receiver;
    }
    
    public void setTap(Node tap)
    {
        synchronized(this)
        {
            this.tap=tap;
        }
    }
    public Node getTap()
    {
        synchronized(this)
        {
            return this.tap;
        }
    }

    
    @Override
    public void process(Packet packet) throws Throwable
    {
        this.receiver.process(packet);
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.process(packet);
            }
        }
    }

    @Override
    public void flush() throws Throwable
    {
        this.receiver.flush();
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.flush();
            }
        }
    }

    @Override
    public void beginGroup(long groupIdentifier) throws Throwable
    {
        this.receiver.beginGroup(groupIdentifier);
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.beginGroup(groupIdentifier);
            }
        }
    }

    @Override
    public void endGroup() throws Throwable
    {
        this.receiver.endGroup();
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.endGroup();
            }
        }
    }

}
