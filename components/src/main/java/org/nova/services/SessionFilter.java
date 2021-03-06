/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.services;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
        for (AbnormalSessionRequestHandling handler:abnormalSessionHandlers)
        {
            for (String mediaType:handler.getMediaTypes())
            {
                this.abnormalSessionHandlers.put(mediaType, handler);
                
            }
        }
    }

    public SessionFilter(SessionManager<?> sessionManager,String tokenKey,AbnormalSessionRequestHandling...abnormalSessionHandlers)
    {
        this(sessionManager,tokenKey,tokenKey,tokenKey,abnormalSessionHandlers);
    }
    public void setAbnormalSessionHandler(AbnormalSessionRequestHandling handler)
    {
        for (String mediaType:handler.getMediaTypes())
        {
            this.abnormalSessionHandlers.put(mediaType, handler);
            
        }
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

        AbnormalSessionRequestHandling abnormal=this.abnormalSessionHandlers.get(contentType);
        if (abnormal!=null)
        {
            return abnormal;
        }
        int index=contentType.indexOf('/');
        if (index>0)
        {
            contentType=contentType.substring(0, index)+"*";
            abnormal=this.abnormalSessionHandlers.get(contentType);
            if (abnormal!=null)
            {
                return abnormal;
            }
        }
        return this.abnormalSessionHandlers.get("*/*");
        
    }
    
    public Session getSession(Context context)
    {
        String token=getToken(context);
        return this.sessionManager.getSessionByToken(token);
    }

    private String getToken(Context context)
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
        return token;
    }

    
    
    @Override
    public Response<?> executeNext(Trace parent, Context context, FilterChain filterChain) throws Throwable
    {
        String token=getToken(context);
        Session session=this.sessionManager.getSessionByToken(token);

        Method method=context.getRequestHandler().getMethod();
        if (method.getAnnotation(AllowNoSession.class)!=null)
        {
            if (session!=null)
            {
                context.setState(session);
            }
            return filterChain.next(parent, context);
        }
        if (session==null)
        {
            if (this.debugSession==null)
            {
                Response<?> response=getAbnormalSessionRequestHandler(context).handleNoSessionRequest(parent,this, context);
                if (response!=null)
                {
                    return response;
                }
                // handleNoSessionRequest() can create a valid session, so we check again.
                session=this.sessionManager.getSessionByToken(token);
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
            return getAbnormalSessionRequestHandler(context).handleNoLockRequest(parent,this, session, context);
        }
        session.beginSessionProcessing(lock);
        try
        {
            if (session.isAccessDenied(parent,context))
            {
                return getAbnormalSessionRequestHandler(context).handleAccessDeniedRequest(parent,this, session, context);
            }
            context.setState(session);
            return filterChain.next(parent, context);
        }
        finally
        {
            session.endSessionProcessing();
            HttpServletResponse response=context.getHttpServletResponse();
            response.setHeader("Cache-Control","no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
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
