package org.nova.services;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import org.nova.concurrent.Lock;
import org.nova.http.server.Context;
import org.nova.http.server.Filter;
import org.nova.http.server.FilterChain;
import org.nova.http.server.Response;
import org.nova.tracing.Trace;


public class SessionFilter extends Filter
{
    final private SessionManager<?> sessionManager;
    final private String headerTokenKey;
    final private String queryTokenKey;
    final private String cookieTokenKey;
    final private HashMap<String,AbnormalSessionRequestHandling> abnormalSessionHandlers;
    private Session debugSession;
    
    public SessionFilter(SessionManager<?> sessionManager,String headerTokenKey,String queryTokenKey,String cookieTokenKey,AbnormalSessionRequestHandling...abnormalSessionHandlers)
    {
        this.sessionManager=sessionManager;
        this.headerTokenKey=headerTokenKey;
        this.queryTokenKey=queryTokenKey;
        this.cookieTokenKey=cookieTokenKey;
        this.abnormalSessionHandlers=new HashMap<>();
        this.abnormalSessionHandlers.put("*/*", new DefaultAbnormalSessionRequestHandler());
        for (AbnormalSessionRequestHandling responder:abnormalSessionHandlers)
        {
            for (String mediaType:responder.getMediaTypes())
            {
                this.abnormalSessionHandlers.put(mediaType, responder);
                
            }
        }
    }

    public SessionFilter(SessionManager<?> sessionManager,String tokenKey,AbnormalSessionRequestHandling...abnormalSessionHandlers)
    {
        this(sessionManager,tokenKey,tokenKey,tokenKey,abnormalSessionHandlers);
    }

    public void setDebugSession(Session session)
    {
        this.debugSession=session;
    }
    
    private AbnormalSessionRequestHandling getAbnormalSessionRequestHandler(Context context)
    {
        String contentType=context.getHttpServletRequest().getContentType();
        if (contentType==null)
        {
            contentType="*/*";
        }

        AbnormalSessionRequestHandling responder=this.abnormalSessionHandlers.get(contentType);
        if (responder!=null)
        {
            return responder;
        }
        int index=contentType.indexOf('/');
        if (index>0)
        {
            contentType=contentType.substring(0, index)+"*";
            responder=this.abnormalSessionHandlers.get(contentType);
            if (responder!=null)
            {
                return responder;
            }
        }
        return this.abnormalSessionHandlers.get("*/*");
        
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
                session=getAbnormalSessionRequestHandler(context).handleNoSessionRequest(parent,this, context);
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
            getAbnormalSessionRequestHandler(context).handleNoLockRequest(parent,this, session, context);
            return null;
        }
        session.update(lock);
        try
        {
            if (session.isAccessDenied(parent,context))
            {
                getAbnormalSessionRequestHandler(context).handleAccessDeniedRequest(parent,this, session, context);
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
}