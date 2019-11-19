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
package org.nova.io;

import java.io.IOException;
import java.io.InputStream;

/* Allows the io to be synchronized by a lock.
 * Thus, a disk level lock can now synchronize access to disk to reduce disk head seeks.
 */

public class LockInputStream extends InputStream
{
    private final InputStream inputStream;
    private final Object lock;

    public LockInputStream(InputStream inputStream, Object lock)
    {
        this.inputStream = inputStream;
        this.lock=lock;
    }

    @Override
    public int read() throws IOException
    {
        synchronized (this.lock)
        {
            return this.inputStream.read();
        }
    }

    public int read(byte b[], int off, int len) throws IOException
    {
        synchronized (this.lock)
        {
            return this.inputStream.read(b, off, len);
        }
    }

    public long skip(long n) throws IOException
    {
        synchronized (this.lock)
        {
            return this.inputStream.skip(n);
        }
    }

    public int available() throws IOException
    {
        synchronized (this.lock)
        {
            return this.inputStream.available();
        }
    }

    public void close() throws IOException
    {
        synchronized (this.lock)
        {
            this.inputStream.close();
        }
    }

    public synchronized void mark(int readlimit)
    {
        synchronized (this.lock)
        {
            this.inputStream.mark(readlimit);
        }
    }

    public synchronized void reset() throws IOException
    {
        synchronized (this.lock)
        {
            this.inputStream.reset();
        }
    }

    public boolean markSupported()
    {
        synchronized (this.lock)
        {
            return this.inputStream.markSupported();
        }
    }

}
