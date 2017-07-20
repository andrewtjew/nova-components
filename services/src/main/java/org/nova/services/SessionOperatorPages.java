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
import org.nova.html.tags.form_get;
import org.nova.html.tags.hr;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_submit;
import org.nova.html.tags.meta;
import org.nova.html.tags.td;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.BasicPage;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.enums.http_equiv;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.HtmlUtils;
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
@ContentWriters({JSONContentWriter.class,AjaxQueryResultWriter.class,HtmlElementWriter.class})
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
        page.content().addInner(new hr());
        OperatorTable table=page.content().returnAddInner(new ServerOperatorPages.OperatorTable(page.head()));
        table.setHeadRow(new Row()
                .add("Token","User","Created","Last Accessed","Active","Idle","Accessed","Rate","")
                );
        
        for (Session session:this.sessionManager.getSessionSnapshot())
        {
            long duration=System.currentTimeMillis()-session.getCreated();
            long idle=System.currentTimeMillis()-session.getLastAccess();
            Row row=new Row();
            row.add(session.getToken(),session.getUser(),Utils.millisToLocalDateTimeString(session.getCreated()),Utils.millisToLocalDateTimeString(session.getLastAccess()),Utils.millisToDurationString(duration),Utils.millisToDurationString(idle)
                    ,session.getAccessRateMeter().getCount(),String.format("%.2f",session.getAccessRateMeter().sampleRate(10)
                            ));
            /*
            row.addRemoveAndDetailButtons("window.location='"+new PathAndQueryBuilder("/operator/sessions/remove").addQuery("token", session.getToken()).toString()+"';"
                    ,new PathAndQueryBuilder("/operator/session").addQuery("token",session.getToken()).toString()
                    );
            */
            row.addDetailButton(new PathAndQueryBuilder("/operator/session").addQuery("token",session.getToken()).toString()
                    );
           table.addBodyRow(row);
        }
        return page;
    }   

    @GET
    @Path("/operator/sessions/remove")
    public Element delete(Trace parent,Context context,@QueryParam("token") String token) throws Exception, Throwable
    {
        this.sessionManager.removeSessionByToken(parent, token);
        return HtmlUtils.redirect("/operator/sessions");
    }   

    @GET
    @Path("/operator/session")
    public Element getSession(Trace parent,@QueryParam("token") String token) throws Exception, Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Session Info");
        /*
        OperatorTable table=page.content().returnAddInner(new ServerOperatorPages.OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Name","Value"));
        
        SESSION session= this.sessionManager.getSessionByToken(token);
        for (NameObject item:session.getDisplayItems())
        {
            table.addBodyRow(new Row().add(item.getName(),item.getValue()));
        }
        */
        SESSION session= this.sessionManager.getSessionByToken(token);
        NameValueList list=page.content().returnAddInner(new NameValueList());
        for (NameObject item:session.getDisplayItems())
        {
            list.add(item.getName(),item.getValue());
        }
        return page;
    }   

}
