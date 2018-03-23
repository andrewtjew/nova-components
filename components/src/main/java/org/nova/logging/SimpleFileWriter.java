package org.nova.logging;

import java.io.OutputStream;

import org.nova.logging.Formatter;
import org.nova.logging.LogDirectoryManager;

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