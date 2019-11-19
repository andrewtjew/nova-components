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

public class BooleanStateSample
{
    final private long lastChangeMs;
    final private long lastUpdateMs;
    final private long lastNoChangeUpdates;
    final private boolean state;
    final private long changeUpdates;
    final private long noChangeUpdates;
    
    BooleanStateSample(long lastChangeMs,long lastUpdateMs,long lastNoChangeUpdates,boolean state,long changeUpdates,long noChangeUpdates)
    {
        this.lastChangeMs=lastChangeMs;
        this.lastUpdateMs=lastUpdateMs;
        this.lastNoChangeUpdates=lastNoChangeUpdates;
        this.state=state;
        this.changeUpdates=changeUpdates;
        this.noChangeUpdates=noChangeUpdates;
    }

    public long getLastChangeMs()
    {
        return lastChangeMs;
    }

    public long getLastUpdateMs()
    {
        return lastUpdateMs;
    }

    public long getLastNoChangeUpdates()
    {
        return lastNoChangeUpdates;
    }

    public boolean getState()
    {
        return state;
    }

    public long getChangeUpdates()
    {
        return changeUpdates;
    }

    public long getNoChangeUpdates()
    {
        return noChangeUpdates;
    }
    
}
