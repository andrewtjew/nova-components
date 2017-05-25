package org.nova.services;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.nova.core.NameObject;
import org.nova.core.Utils;
import org.nova.frameworks.OperatorPage;
import org.nova.frameworks.ServerApplication;
import org.nova.frameworks.ServerOperatorPages;
import org.nova.frameworks.ServerOperatorPages.OperatorTable;
import org.nova.frameworks.ServerOperatorPages.WideTable;
import org.nova.html.HtmlWriter;
import org.nova.html.elements.Element;
import org.nova.html.operator.OperatorResult;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.tags.form_get;
import org.nova.html.tags.hr;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_submit;
import org.nova.html.tags.td;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.NameValueList;
import org.nova.html.widgets.Row;
import org.nova.http.client.PathAndQueryBuilder;
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
@ContentWriters({OperatorResultWriter.class,JSONContentWriter.class,AjaxQueryResultWriter.class,HtmlElementWriter.class})
public class SessionOperatorPages<SESSION extends Session>
{
    final private SessionManager<SESSION> sessionManager;
    final private ServerApplication serverApplication;
    public SessionOperatorPages(SessionManager<SESSION> sessionManager,ServerApplication serverApplication)
    {
        this.sessionManager=sessionManager;
        this.serverApplication=serverApplication;
    }
    
    @GET
    @Path("/operator/sessions")
    public Element getAll(Trace parent) throws Exception, Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("All Sessions");
        form_get form=page.content().returnAddInner(new form_get()
                .action("/operator/sessions/remove")
                );
        form.returnAddInner(new input_submit().value("Remove sessions"));
        page.content().addInner(new hr());
        OperatorTable table=page.content().returnAddInner(new ServerOperatorPages.OperatorTable(page.head()));
        table.setHeadRow(new Row()
                .add("Token","User","Created","Last Accessed","Active","Idle","Accessed","Rate")
                );
        
        this.sessionManager.getSessionSnapshot().stream().
        sorted((a,b)->Utils.
        compare(a.getUser(),b.getUser())).
        forEach(session->
        {
            long duration=System.currentTimeMillis()-session.getCreated();
            long idle=System.currentTimeMillis()-session.getLastAccess();
            Row row=new Row();
            row.addInner(
                    new td()
                    .addInner(
                            new input_checkbox().name(session.getToken())
                    ));
            try
            {
                row.addWithUrl(session.getToken(),new PathAndQueryBuilder("/operator/session").addQuery("token",session.getToken()).toString(),true);
            }
            catch (Exception e)
            {
            }
            row.add(session.getUser(),Utils.millisToLocalDateTimeString(session.getCreated()),Utils.millisToLocalDateTimeString(session.getLastAccess()),Utils.millisToDurationString(duration),Utils.millisToDurationString(idle)
                    ,session.getAccessRateMeter().getCount(),String.format("%.2f",session.getAccessRateMeter().sampleRate(10)
                            ));
        });
        return page;
    }   

    @GET
    @Path("/operator/sessions/remove")
    public Element delete(Trace parent,Context context) throws Exception, Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Session Info");
        WideTable table=page.content().returnAddInner(new ServerOperatorPages.WideTable(page.head()));
        table.setHeadRow(new Row().add("Session token","Remove result"));
        HttpServletRequest request=context.getHttpServletRequest();
        int success=0;
        int failures=0;
        for (String entry:request.getParameterMap().keySet())
        {
            if (this.sessionManager.removeSessionByToken(parent, entry))
            {
                table.addBodyRow(new Row().add(entry,"OK"));
                success++;
            }
            else
            {
                table.addBodyRow(new Row().add(entry,"FAILED"));
                failures++;
            }
        }
        NameValueList list=page.content().returnAddInner(new NameValueList());
        list.add("Removed", success);
        list.add("Failures", failures);
        return page;
    }   

    @GET
    @Path("/operator/session")
    public Element getSession(Trace parent,@QueryParam("token") String token) throws Exception, Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Session Info");
        OperatorTable table=page.content().returnAddInner(new ServerOperatorPages.OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Name","Value"));
        
        SESSION session= this.sessionManager.getSessionByToken(token);
        for (NameObject item:session.getDisplayInfo())
        {
            table.addBodyRow(new Row().add(item.getName(),item.getValue()));
        }
        return page;
    }   

}
