package org.nova.services;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public class DefaultNoSessionResponder extends NoSessionResponder
{
    
    @Override
    public Session respondToNoSession(Trace parent,SessionFilter sessionFilter,Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        String endPoint=sessionFilter.getDirectoryServiceEndPoint();
        if (endPoint==null)
        {
            response.addHeader("Location", endPoint);
        }
        response.setStatus(HttpStatus.UNAUTHORIZED_401);
        return null;
    }

    @Override
    public void respondToAccessDenied(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    public void respondToNoLock(Trace parent,SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    public String getAssociatedMediaType()
    {
        return null;
    }

}
