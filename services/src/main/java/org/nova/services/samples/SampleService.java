package org.nova.services.samples;

import java.util.ArrayList;

import org.nova.core.NameObject;
import org.nova.frameworks.CoreEnvironment;
import org.nova.frameworks.ServerApplicationRunner;
import org.nova.http.server.HttpServer;
import org.nova.services.AccessSession;
import org.nova.services.SessionServerApplication;
import org.nova.tracing.Trace;

public class SampleService extends SessionServerApplication<SampleUserSession>
{
    static public class SampleUserSession extends AccessSession<SampleService>
    {
        public SampleUserSession(SampleService service,String token,String user)
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
            // Add as appropriate
            return list.toArray(new NameObject[list.size()]);
        }

        @Override
        public boolean isInGroup(String group)
        {
            return false;
        }
    }
    
    public static void main(String[] args) throws Throwable
    {
        new ServerApplicationRunner().run(args,(coreEnvironment,operatorServer)->{return new SampleService(coreEnvironment,operatorServer);});
    }


    public SampleService(CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable
    {
        super("Template", coreEnvironment,operatorServer);
    }
    
    public void onStart(Trace parent) throws Throwable
    {
        Trace trace=new Trace(parent, "testcategory");
        trace.close(new Exception("test"));
    }
    
}
