package org.nova.logging;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.nova.logging.Formatter;
import org.nova.logging.LogDirectoryManager;
import org.nova.metrics.RateMeter;
import org.nova.test.Testing;

import net.jpountz.lz4.LZ4BlockOutputStream;

public class SimpleFileWriter extends OutputStreamWriter
{
    final private LogDirectoryManager logDirectoryManager;

    public SimpleFileWriter(LogDirectoryManager logDirectoryManager,Formatter formatter) throws Throwable
    {
        super(formatter);
        this.logDirectoryManager = logDirectoryManager;
    }

    @Override
    public OutputStream openOutputStream(long marker) throws Throwable
    {
        return this.logDirectoryManager.openFileOutputStream(marker, ".txt");
    }

    @Override
    public void closeOutputStream(OutputStream outputStream) throws Throwable
    {
        outputStream.close();
    }
}