package org.nova.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.server.Context;
import org.nova.tracing.Trace;

public class BrowserNoSessionResponder extends NoSessionResponder
{
    final private String noSessionURL;
    final private String accessDeniedURL;
    final private String noLockURL;
    
    public BrowserNoSessionResponder(String noSessionURL,String accessDeniedURL,String noLockURL)
    {
        this.noLockURL=noLockURL;
        this.accessDeniedURL=accessDeniedURL;
        this.noSessionURL=noSessionURL;
    }
    
    @Override
    public Session respondToNoSession(Trace parent,SessionFilter sessionFilter, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", noSessionURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
        return null;
    }

    @Override
    public void respondToAccessDenied(Trace parent,SessionFilter sessionFilter, Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", accessDeniedURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
    }

    @Override
    public void respondToNoLock(Trace parent,SessionFilter sessionFilter, Session session, Context context)
    {
        HttpServletResponse response=context.getHttpServletResponse();
        response.addHeader("Location", noLockURL);
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
    }

    @Override
    public String getAssociatedMediaType()
    {
        return "text/html";
    }

}
