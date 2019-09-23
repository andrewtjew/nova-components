package org.samples;

import java.util.ArrayList;
import org.nova.core.NameObject;
import org.nova.http.server.Context;
import org.nova.services.AccessSession;
import org.nova.services.Session;
import org.nova.tracing.Trace;


public class UserSession extends AccessSession<Service>
{
    public UserSession(Service service,String token,String user)
    {
        super(token, user);
    }

    @Override
    public void onClose(Trace trace) throws Throwable
    {
    }

    @Override
    public NameObject[] getDisplayItems()
    {
        ArrayList<NameObject> list=new ArrayList<>();
        // Add
        return list.toArray(new NameObject[list.size()]);
    }

    @Override
    public boolean isInGroup(String group)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
