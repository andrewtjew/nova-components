package org.nova.logging;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.nova.logging.Formatter;
import org.nova.logging.LogDirectoryManager;
import org.nova.metrics.RateMeter;
import org.nova.test.Testing;

import net.jpountz.lz4.LZ4BlockOutputStream;

public class BufferedLZ4FileWriter extends OutputStreamWriter
{
    private final ByteArrayOutputStream byteArrayOuputStream;
    final private LogDirectoryManager logDirectoryManager;
    private long marker;

    public BufferedLZ4FileWriter(LogDirectoryManager logDirectoryManager,int initialCapacity, Formatter formatter, RateMeter rateMeter) throws Throwable
    {
        super(formatter,rateMeter);
        this.byteArrayOuputStream=new ByteArrayOutputStream(initialCapacity);
        this.logDirectoryManager = logDirectoryManager;
    }

    @Override
    public OutputStream openOutputStream(long marker) throws Throwable
    {
        this.marker = marker;
        this.byteArrayOuputStream.reset();
        return new LZ4BlockOutputStream(this.byteArrayOuputStream);
    }

    @Override
    public void closeOutputStream(OutputStream outputStream) throws Throwable
    {
        outputStream.close(); // This flushes to the underlying stream.
        if (this.byteArrayOuputStream.size()>0)
        {
//            Testing.println("BufferedLZ4FileWriter: write");
            this.logDirectoryManager.write(this.byteArrayOuputStream,marker,".lz4");
        }
    }
}