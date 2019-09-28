package org.nova.services;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Response;
import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public class DefaultAbnormalSessionRequestHandler implements AbnormalSessionRequestHandling
{
    
    @Override
    public Response<?> handleNoSessionRequest(Trace parent,SessionFilter sessionFilter,Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED_401);
        return null;
    }

    @Override
    public Response<?> handleAccessDeniedRequest(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.FORBIDDEN_403);
        return null;
    }

    @Override
    public Response<?> handleNoLockRequest(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
        return null;
    }

    @Override
    public String[] getMediaTypes()
    {
        return new String[]{"*/*"};
    }

}
