package org.nova.services;

import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public interface AbnormalSessionRequestHandling
{
    public Session handleNoSessionRequest(Trace parent,SessionFilter sessionFilter,Context context);
    public void handleAccessDeniedRequest(Trace parent,SessionFilter sessionFilter,Session session,Context context);
    public void handleNoLockRequest(Trace parent,SessionFilter sessionFilter,Session session,Context context);
    public String[] getMediaTypes();
}
