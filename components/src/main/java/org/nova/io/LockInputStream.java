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
