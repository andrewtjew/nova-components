package org.nova.logging;

import org.nova.tracing.Trace;

public class NullLogger extends Logger
{
    public NullLogger()
    {
        super("null");
    }

    @Override
    public void log(Trace trace, Level logLevel, String category, Throwable throwable, String message, Item[] items)
    {
    }

}
