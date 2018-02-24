package org.nova.services;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public class DefaultAbnormalSessionRequestHandler implements AbnormalSessionRequestHandling
{
    
    @Override
    public Session handleNoSessionRequest(Trace parent,SessionFilter sessionFilter,Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED_401);
        return null;
    }

    @Override
    public void handleAccessDeniedRequest(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    public void handleNoLockRequest(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    public String[] getMediaTypes()
    {
        return new String[]{"*/*"};
    }

}
