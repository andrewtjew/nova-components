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
package org.nova.metrics;

import org.nova.collections.RingBuffer;
import org.nova.tracing.Trace;

public class TraceHistoryMeter
{
    final private RingBuffer<Trace> traces;
    final private RingBuffer<Trace> exceptionTraces;
    
    private Trace firstRecentChainExceptionTrace;
    private long recentChainExceptionTraces;
    private long lastChainNormalTraces;
    
    public TraceHistoryMeter(int capacity)
    {
        this.traces=new RingBuffer<>(new Trace[capacity]);
        this.exceptionTraces=new RingBuffer<>(new Trace[capacity]);
    }
    public void update(Trace trace)
    {
        synchronized (this)
        {
            this.traces.add(trace);
            if (trace.getThrowable()!=null)
            {
                this.exceptionTraces.add(trace);
                if (this.lastChainNormalTraces!=0)
                {
                    this.lastChainNormalTraces=0;
                    this.firstRecentChainExceptionTrace=trace;
                    this.recentChainExceptionTraces=0;
                }
                this.recentChainExceptionTraces++;
            }
            else
            {
                this.lastChainNormalTraces++;
            }
        }
    }
    
    public TraceHistorySample sample()
    {
        synchronized (this)
        {
            return new TraceHistorySample(this.traces.getSnapshot(), this.exceptionTraces.getSnapshot(), this.firstRecentChainExceptionTrace, this.recentChainExceptionTraces, this.lastChainNormalTraces);
        }
    }
}
