package org.nova.tracing;

public class TraceSettings
{
    final long number;
    final boolean captureCreateStack;
    final boolean captureCloseStack;
    
    TraceSettings(long number,boolean captureCreateStack,boolean capturecloseStack)
    {
        this.number=number;
        this.captureCreateStack=captureCreateStack;
        this.captureCloseStack=capturecloseStack;
      
    }
}
