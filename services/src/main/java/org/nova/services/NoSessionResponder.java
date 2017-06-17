package org.nova.services;

import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public abstract class NoSessionResponder
{
    public abstract Session respondToNoSession(Trace parent,SessionFilter sessionFilter,Context context);
    public abstract void respondToAccessDenied(Trace parent,SessionFilter sessionFilter,Session session,Context context);
    public abstract void respondToNoLock(Trace parent,SessionFilter sessionFilter,Session session,Context context);
    public abstract String getAssociatedMediaType();
}
