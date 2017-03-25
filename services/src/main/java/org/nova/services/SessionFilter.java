package org.nova.services;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.concurrent.Lock;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;
import org.nova.http.server.Filter;
import org.nova.http.server.FilterChain;
import org.nova.http.server.Response;
import org.nova.tracing.Trace;


public class SessionFilter extends Filter
{
    final private SessionManager<?> sessionManager;
    final private String directoryServiceEndPoint;
    final private String headerTokenKey;
    final private String queryTokenKey;
    final private String cookieTokenKey;
    final private DefaultSessionRejectResponder defaultResponder;
    final private HashMap<String,SessionRejectResponder> sessionRejectResponders;
    public SessionFilter(SessionManager<?> sessionManager,String directoryServiceEndPoint,String headerTokenKey,String queryTokenKey,String cookieTokenKey,SessionRejectResponder...sessionRejectResponders)
    {
        this.sessionManager=sessionManager;
        this.directoryServiceEndPoint=directoryServiceEndPoint;
        this.headerTokenKey=headerTokenKey;
        this.queryTokenKey=queryTokenKey;
        this.cookieTokenKey=cookieTokenKey;
        this.defaultResponder=new DefaultSessionRejectResponder();
        this.sessionRejectResponders=new HashMap<>();
        for (SessionRejectResponder responder:sessionRejectResponders)
        {
            this.sessionRejectResponders.put(responder.getAssociatedMediaType(), responder);
        }
    }
    
    private SessionRejectResponder getBestSessionRejectResponder(Context context)
    {
        ContentWriter<?> contentWriter=context.getContentWriter();
        if (contentWriter==null)
        {
            return this.defaultResponder;
        }
        SessionRejectResponder responder=this.sessionRejectResponders.get(contentWriter.getMediaType());
        if (responder==null)
        {
            return this.defaultResponder;
        }
        return responder;
    }
    
    @Override
    public Response<?> executeNext(Trace trace, Context context, FilterChain filterChain) throws Throwable
    {
        String token=null;
        if (this.headerTokenKey!=null)
        {
            token=context.getHttpServletRequest().getHeader(this.headerTokenKey);
        }
        if ((token==null)&&(this.queryTokenKey!=null))
        {
            token=context.getHttpServletRequest().getParameter(this.queryTokenKey);
        }
        if ((token==null)&&(this.cookieTokenKey!=null))
        {
            Cookie[] cookies=context.getHttpServletRequest().getCookies();
            if (cookies!=null)
            {
                for (Cookie cookie:cookies)
                {
                    if (this.cookieTokenKey.equals(cookie.getName()))
                    {
                        token=cookie.getValue();
                        break;
                    }
                }
            }
        }
        Session session=this.sessionManager.getSessionByToken(token);
        if (session==null)
        {
            getBestSessionRejectResponder(context).respondToNoSession(this, context);
            return null;
        }
        Lock<String> lock=sessionManager.waitForLock(session.getToken());
        if (lock==null)
        {
            getBestSessionRejectResponder(context).respondToNoLock(this, session, context);
            return null;
        }
        if (session.isAccessDeniedForCurrentRequest(trace,context))
        {
            getBestSessionRejectResponder(context).respondToAccessDenied(this, session, context);
            return null;
        }
        session.update(lock);
        context.setState(session);
        try
        {
            return filterChain.next(trace, context);
        }
        finally
        {
            session.closeLock();
        }
        
    }

    public String getHeaderTokenKey()
    {
        return headerTokenKey;
    }

    public String getQueryTokenKey()
    {
        return queryTokenKey;
    }

    public String getCookieTokenKey()
    {
        return cookieTokenKey;
    }
    public String getDirectoryServiceEndPoint()
    {
        return this.directoryServiceEndPoint;
    }
}