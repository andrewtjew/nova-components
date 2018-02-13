package org.nova.logging;

import java.io.OutputStream;

import org.nova.core.Utils;
import org.nova.logging.Formatter;

public class ConsoleWriter extends OutputStreamWriter
{
    final private boolean outputSegments; 
    public ConsoleWriter(Formatter formatter,boolean outputSegments) throws Throwable
    {
        super(formatter);
        this.outputSegments=outputSegments;
    }

    @Override
    public OutputStream openOutputStream(long marker) throws Throwable
    {
        if (this.outputSegments)
        {
            System.out.println("--- Begin Segment: Marker="+marker+", Time="+Utils.millisToLocalDateTimeString(marker));
        }
        return System.out;
    }
    @Override
    public void closeOutputStream(OutputStream outputStream)
    {
        if (this.outputSegments)
        {
            System.out.println("--- End Segment ---");
        }
    }

}
