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

public class AccessLimiter
{
    private long deniedCount;
    private long allowedCount;
    private int available;
    private long last;
    final private int maxAllowedPerInterval;
    final private long intervalNs;
    
    public AccessLimiter(int maxAllowedPerInterval,double allowedInterval,int firstAvailable)
    {
        this.available=firstAvailable;
        this.maxAllowedPerInterval=maxAllowedPerInterval;
        this.intervalNs=(long)(allowedInterval*1.0e9);
        this.last=System.nanoTime();
    }
    
    public boolean isAccessAllowedAndUpdate()
    {
        if (this.available>0)
        {
            this.available--;
            this.allowedCount++;
            return true;
        }
        long now=System.nanoTime();
        if (now-this.last<intervalNs)
        {
            this.deniedCount++;
            return false;
        }
        this.last=now;
        this.available=this.maxAllowedPerInterval-1;
        this.allowedCount++;
        return true;
    }

    public long getDeniedCount()
    {
        return deniedCount;
    }

    public long getAllowedCount()
    {
        return allowedCount;
    }
}
