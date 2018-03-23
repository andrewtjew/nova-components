package org.nova.tracing;

public class TraceContext
{
    final long number;
    final boolean captureCreateStack;
    final boolean captureCloseStack;
    
    TraceContext(long number,boolean captureCreateStack,boolean capturecloseStack)
    {
        this.number=number;
        this.captureCreateStack=captureCreateStack;
        this.captureCloseStack=capturecloseStack;
    }
}
