/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
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
package org.nova.metrics;

public class BooleanStateMeter
{
    private long lastChangeMs;
    private long lastUpdateMs;
    private boolean state;
    private long changeUpdates;
    private long noChangeUpdates;
    private long lastNoChangeUpdates;
    
    public BooleanStateMeter(boolean state)
    {
        synchronized(this)
        {
            this.lastUpdateMs=this.lastChangeMs=System.currentTimeMillis();
            this.state=state;
        }
    }
    
    public void update(boolean state)
    {
        synchronized(this)
        {
            this.lastUpdateMs=System.currentTimeMillis();
            if (state!=this.state)
            {
                this.changeUpdates++;
                this.lastChangeMs=this.lastUpdateMs;
                this.state=state;
            }
            else
            {
                this.lastNoChangeUpdates++;
            }
        }
    }

    public BooleanStateSample sample()
    {
        synchronized(this)
        {
            return new BooleanStateSample(this.lastChangeMs,this.lastUpdateMs,this.lastNoChangeUpdates,this.state,this.changeUpdates,this.noChangeUpdates);
        }
    }
    
    
}
