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
    final private DefaultNoSessionResponder defaultResponder;
    final private HashMap<String,NoSessionResponder> noSessionResponders;
    private Session debugSession;
    
    public SessionFilter(SessionManager<?> sessionManager,String directoryServiceEndPoint,String headerTokenKey,String queryTokenKey,String cookieTokenKey,NoSessionResponder...noRejectResponders)
    {
        this.sessionManager=sessionManager;
        this.directoryServiceEndPoint=directoryServiceEndPoint;
        this.headerTokenKey=headerTokenKey;
        this.queryTokenKey=queryTokenKey;
        this.cookieTokenKey=cookieTokenKey;
        this.defaultResponder=new DefaultNoSessionResponder();
        this.noSessionResponders=new HashMap<>();
        for (NoSessionResponder responder:noRejectResponders)
        {
            this.noSessionResponders.put(responder.getAssociatedMediaType(), responder);
        }
    }
    public void setDebugSession(Session session)
    {
        this.debugSession=session;
    }
    
    private NoSessionResponder getBestSessionRejectResponder(Context context)
    {
        ContentWriter<?> contentWriter=context.getContentWriter();
        if (contentWriter==null)
        {
            return this.defaultResponder;
        }
        NoSessionResponder responder=this.noSessionResponders.get(contentWriter.getMediaType());
        if (responder==null)
        {
            return this.defaultResponder;
        }
        return responder;
    }
    
    @Override
    public Response<?> executeNext(Trace parent, Context context, FilterChain filterChain) throws Throwable
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
            if (this.debugSession==null)
            {
                session=getBestSessionRejectResponder(context).respondToNoSession(parent,this, context);
                if (session==null)
                {
                    return null;
                }
            }
            else
            {
                session=this.debugSession;
            }
        }
        Lock<String> lock=sessionManager.waitForLock(parent,session.getToken());
        if (lock==null)
        {
            getBestSessionRejectResponder(context).respondToNoLock(parent,this, session, context);
            return null;
        }
        session.update(lock);
        try
        {
            if (session.isAccessDeniedForCurrentRequest(parent,context))
            {
                getBestSessionRejectResponder(context).respondToAccessDenied(parent,this, session, context);
                return null;
            }
            context.setState(session);
            return filterChain.next(parent, context);
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