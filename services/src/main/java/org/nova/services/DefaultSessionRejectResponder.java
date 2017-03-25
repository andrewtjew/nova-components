package org.nova.services;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Context;

public class DefaultSessionRejectResponder extends SessionRejectResponder
{
    
    @Override
    void respondToNoSession(SessionFilter sessionFilter,Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        String endPoint=sessionFilter.getDirectoryServiceEndPoint();
        if (endPoint==null)
        {
            response.addHeader("Location", endPoint);
        }
        response.setStatus(HttpStatus.UNAUTHORIZED_401);
    }

    @Override
    void respondToAccessDenied(SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    void respondToNoLock(SessionFilter sessionFilter,Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.setStatus(HttpStatus.CONFLICT_409);
    }

    @Override
    String getAssociatedMediaType()
    {
        return null;
    }

}
