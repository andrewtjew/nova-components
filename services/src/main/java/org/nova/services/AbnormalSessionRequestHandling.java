package org.nova.services;

import org.nova.http.server.Context;
import org.nova.http.server.Response;
import org.nova.tracing.Trace;

public interface AbnormalSessionRequestHandling
{
    public Response<?> handleNoSessionRequest(Trace parent,SessionFilter sessionFilter,Context context) throws Throwable;
    public Response<?> handleAccessDeniedRequest(Trace parent,SessionFilter sessionFilter,Session session,Context context) throws Throwable;;
    public Response<?> handleNoLockRequest(Trace parent,SessionFilter sessionFilter,Session session,Context context) throws Throwable;;
    public String[] getMediaTypes();
}
