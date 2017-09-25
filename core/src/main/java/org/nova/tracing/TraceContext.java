package org.nova.tracing;

import org.nova.metrics.ValueRateMeter;

public class TraceContext
{
    final long number;
    final boolean captureCreateStack;
    final boolean captureCloseStack;
    final boolean enableTraceGraph;
    final ValueRateMeter valueRateMeter;
    
    TraceContext(long number,boolean captureCreateStack,boolean capturecloseStack,boolean enableTraceGraph,ValueRateMeter valueRateMeter)
    {
        this.number=number;
        this.captureCreateStack=captureCreateStack;
        this.captureCloseStack=capturecloseStack;
        this.enableTraceGraph=enableTraceGraph;
        this.valueRateMeter=valueRateMeter;
    }
}
