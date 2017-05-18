package org.nova.html.elements;

import java.io.OutputStream;

public class OutputStreamBuilder extends Builder
{
    final private OutputStream outputStream;

    public OutputStreamBuilder(OutputStream outputStream)
    {
        this.outputStream=outputStream;
    }
    @Override
    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }
    
}