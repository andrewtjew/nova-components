package org.nova.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Context;

public class BrowserSessionRejectResponder extends SessionRejectResponder
{
    final private String noSessionURL;
    final private String accessDeniedURL;
    final private String noLockURL;
    
    public BrowserSessionRejectResponder(String noSessionURL,String accessDeniedURL,String noLockURL)
    {
        this.noLockURL=noLockURL;
        this.accessDeniedURL=accessDeniedURL;
        this.noSessionURL=noSessionURL;
    }
    
    @Override
    void respondToNoSession(SessionFilter sessionFilter, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", noSessionURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
    }

    @Override
    void respondToAccessDenied(SessionFilter sessionFilter, Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", accessDeniedURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
    }

    @Override
    void respondToNoLock(SessionFilter sessionFilter, Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", noLockURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
    }

    @Override
    String getAssociatedMediaType()
    {
        return "text/html";
    }

}
