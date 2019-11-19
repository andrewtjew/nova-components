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
package org.nova.concurrent;

import org.nova.tracing.Trace;

public class LockReservedState implements AutoCloseable
{
    final private ReservableLock lock;
    final private boolean open;
    final private Trace trace;

    LockReservedState(ReservableLock lock,boolean open,Trace trace)
    {
        this.lock=lock;
        this.open=open;
        this.trace=trace;
    }
    public boolean isOpen()
    {
        return this.open;
    }
    
    public ReservableLockState lock(Object object) throws Exception
    {
        if (object==null)
        {
            throw new Exception("object==null");
        }
        if (this.open==false)
        {
            throw new Exception("no reservation");
        }
        this.lock.setLockObject(object);
        return new ReservableLockState(this.lock);
    }
    @Override
    public void close() throws Exception
    {
        this.lock.releaseReservation();
        this.trace.close();
    }
}
