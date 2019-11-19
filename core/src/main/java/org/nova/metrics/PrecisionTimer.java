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

public class PrecisionTimer
{
	private long startNs; 
	private long stopNs;
	private long elapsedNs;
	public PrecisionTimer()
	{
		
	}
	public void start()
	{
		this.startNs=System.nanoTime();
	}
	public long getCurrentElapsedNs()
	{
		return System.nanoTime()-this.startNs;
	}
	public void stop()
	{
	    long now=System.nanoTime();
	    this.elapsedNs+=now-this.startNs;
	    this.startNs=now;
	}
	public void reset()
	{
	    this.elapsedNs=0;
	}
	public void resetStart()
	{
        this.elapsedNs=0;
        this.startNs=System.nanoTime();
	}
	public long getElapsedNs()
	{
		return this.elapsedNs;
	}
    public long getElapsedMs()
    {
        return getElapsedNs()/1000000;
    }
	
    public double getElapsedS()
    {
        return getElapsedNs()/1.0e9;
    }
	
	
}
