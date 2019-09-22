package org.nova.services.samples;

import java.util.ArrayList;
import org.nova.core.NameObject;
import org.nova.http.server.Context;
import org.nova.services.AccessSession;
import org.nova.services.Session;
import org.nova.tracing.Trace;


public class SampleUserSession extends AccessSession<SampleService>
{
    public SampleUserSession(SampleService service,String token,String user)
    {
        super(token, user);
    }

    @Override
    public NameObject[] getDisplayItems()
    {
        ArrayList<NameObject> list=new ArrayList<>();
        // Add as appropriate
        return list.toArray(new NameObject[list.size()]);
    }

    @Override
    public boolean isInGroup(String group)
    {
        return false;
    }

    @Override
    public void onClose(Trace trace) throws Throwable
    {
    }
}

