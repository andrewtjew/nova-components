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
package org.nova.http.server;

public class LatencyTracker
{
    private LatencyDescriptor latencyDescriptor;
    private long updateCount;
    private long totalBeforeExecuteLatencyMs;
    private long totalAfterExecuteLatencyMs;
    
    public LatencyTracker(LatencyDescriptor latencyDescriptor)
    {
        this.latencyDescriptor=latencyDescriptor;
    }

    public void update(long beforeExecuteMs,long afterExecuteMs)
    {
        synchronized(this)
        {
            this.updateCount++;
            if (beforeExecuteMs>0)
            {
                this.totalBeforeExecuteLatencyMs+=beforeExecuteMs;
            }
            if (afterExecuteMs>0)
            {
                this.totalAfterExecuteLatencyMs+=afterExecuteMs;
            }
        }
    }
    
    public LatencyDescriptor getLatencyDescriptor()
    {
        return this.latencyDescriptor;
    }
    
    public void setLatencyDescriptor(LatencyDescriptor latencyDescriptor)
    {
        this.latencyDescriptor=latencyDescriptor;
    }
    
    public LatencyStatistics getLatencyStatistics()
    {
        synchronized(this)
        {
            return new LatencyStatistics(this.updateCount, this.totalBeforeExecuteLatencyMs, this.totalAfterExecuteLatencyMs);
        }
    }
    
}
