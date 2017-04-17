package org.nova.services;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.nova.core.NameObject;
import org.nova.core.Utils;
import org.nova.html.HtmlWriter;
import org.nova.html.operator.OperationResult;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.http.server.Context;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.Response;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.POST;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.QueryParam;
import org.nova.tracing.Trace;

import com.google.common.base.Strings;

@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({JSONContentReader.class,JSONPatchContentReader.class})
@ContentWriters({OperatorResultWriter.class,JSONContentWriter.class,AjaxQueryResultWriter.class})
public class SessionOperatorPages<SESSION extends Session>
{
    final SessionManager<SESSION> sessionManager;
    public SessionOperatorPages(SessionManager<SESSION> sessionManager)
    {
        this.sessionManager=sessionManager;
    }
    
    @GET
    @Path("/operator/sessions")
    public Response<OperationResult> getAll(Trace parent) throws Exception, Throwable
    {
        HtmlWriter writer=new HtmlWriter();

        writer.begin_form("/operator/sessions/remove", "get");
        writer.begin_table(1);
        writer.input_submit("Remove");
        writer.p();
        writer.tr().th().th("Token").th("User").th("Created").th("Last Accessed").th("Active").th("Idle").th("Accessed").th("Rate");
        this.sessionManager.getSessionSnapshot().stream().
        sorted((a,b)->Utils.
        compare(a.getUser(),b.getUser())).
        forEach(session->
        {
            long duration=System.currentTimeMillis()-session.getCreated();
            long idle=System.currentTimeMillis()-session.getLastAccess();
            writer.tr();
            writer.td(writer.inner().input_checkbox(session.getToken(),session.getToken(), false));
            writer.td(writer.inner().a(session.getToken(),"/operator/session?token="+session.getToken()));
            writer.td(session.getUser()).td(Utils.millisToLocalDateTimeString(session.getCreated())).td(Utils.millisToLocalDateTimeString(session.getLastAccess())).td(Utils.millisToDurationString(duration)).td(Utils.millisToDurationString(idle));
            writer.td(session.getAccessRateMeter().getCount()).td(writer.inner().text(String.format("%.2f",session.getAccessRateMeter().sampleRate(10))));
        });
        writer.end_table();
        writer.end_form();
        return OperationResult.respond(writer, "All Sessions");
    }   
/*
1-888-520-9090
broker@ups.com
X7W601LGWS
*/

    @GET
    @Path("/operator/sessions/remove")
    public Response<OperationResult> delete(Trace parent,Context context) throws Exception, Throwable
    {
        HtmlWriter writer=new HtmlWriter();
        HttpServletRequest request=context.getHttpServletRequest();
        int success=0;
        int failures=0;
        for (String entry:request.getParameterMap().keySet())
        {
            if (this.sessionManager.removeSessionByToken(parent, entry))
            {
                writer.p(entry+"...OK");
                success++;
            }
            else
            {
                writer.p(entry+"...FAILED");
                failures++;
            }
        }
        writer.p("Removed: "+success);
        if (failures>0)
        {
            writer.p("Failures: "+failures);
        }
        return OperationResult.respond(writer, "Remove Sessions");
    }   

    @GET
    @Path("/operator/session")
    public Response<OperationResult> getSession(Trace parent,@QueryParam("token") String token) throws Exception, Throwable
    {
        HtmlWriter writer=new HtmlWriter();
        SESSION session= this.sessionManager.getSessionByToken(token);
        if (session!=null)
        {
            writer.begin_table();
            for (NameObject item:session.getDisplayInfo())
            {
                writer.tr().td(item.getName()).td(":").td(item.getValue());
            }
            writer.end_table();
        }
        else
        {
            writer.h3("Session not found");
        }
        return OperationResult.respond(writer, "Session info");
    }   

}
