package org.nova.services;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

import org.nova.concurrent.Lock;
import org.nova.core.NameObject;
import org.nova.frameworks.ServerApplication;
import org.nova.http.server.Context;
import org.nova.metrics.RateMeter;
import org.nova.tracing.Trace;

public abstract class AccessSession <SERVICE extends ServerApplication> extends Session
{
    HashMap<String,Boolean> denyMap;
    
    public AccessSession(String token, String user)
    {
        super(token, user);
        this.denyMap=new HashMap<>();
    }

    @Override
    public boolean isAccessDenied(Trace trace, Context context) throws Throwable
    {
        Boolean deny=this.denyMap.get(context.getRequestHandler().getKey());
        if (deny==null)
        {
            deny=isAccessDenied(context);
            this.denyMap.put(context.getRequestHandler().getKey(),deny);
        }
        return deny;
    }
    
    boolean isAccessDenied(Context context)
    {
        Method method=context.getRequestHandler().getMethod();
        DenyGroups denyGroups=method.getDeclaredAnnotation(DenyGroups.class);
        if (denyGroups!=null)
        {
            if (denyGroups.value().length==0)
            {
                return true; //deny all
            }
            for (String value:denyGroups.value())
            {
                if (isInGroup(value))
                {
                    return true;
                }
            }
        }

        AllowGroups allowGroups=method.getDeclaredAnnotation(AllowGroups.class);
        if (allowGroups==null)
        {
            return true; //deny all
        }
        if (allowGroups.value().length==0)
        {
            return false; //allow all
        }
        for (String value:allowGroups.value())
        {
            if (isInGroup(value))
            {
                return false;
            }
        }
        return true;
    }

    public abstract boolean isInGroup(String group); 
}
