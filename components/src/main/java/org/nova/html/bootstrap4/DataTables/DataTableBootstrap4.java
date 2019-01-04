package org.nova.html.bootstrap4.DataTables;

import org.nova.html.DataTables.DataTableOptions;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.TableRow;
import org.nova.html.elements.Composer;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.tbody;
import org.nova.html.tags.tr;
import org.nova.html.widgets.ObjectBuilder;
import org.nova.html.widgets.TableFooter;
import org.nova.html.widgets.TableHeader;

//!!! Requires jquery

public class DataTableBootstrap4 extends StyleComponent<DataTableBootstrap4>
{
    final private tbody tbody;
    final private DataTableOptions options;

    private TableHeader header;
    private TableFooter footer;

    public DataTableBootstrap4(Head head,DataTableOptions options)
    {
        this(head,options,"https://cdn.datatables.net/v/bs4/dt-1.10.18/datatables.min.css","https://cdn.datatables.net/v/bs4/dt-1.10.18/datatables.min.js");
    }

    public DataTableBootstrap4(Head head)
    {
        this(head,null);
    }

    public DataTableBootstrap4(DataTableOptions options)
    {
        this(null,options);
    }
    
    public DataTableBootstrap4(Head head,DataTableOptions options,String cssFilePath,String scriptFilePath)
    {
        super("table","table");
        this.tbody=new tbody();
        this.options=options;

        if (head!=null)
        {
            String key=DataTableBootstrap4.class.getCanonicalName();
            script script=new script().src(scriptFilePath);
            head.add(key,script);
            link link=new link().rel(link_rel.stylesheet).type("text/css").href(cssFilePath);
            head.add(key,link);
        }
    }
    public void setHeader(TableHeader header)
    {
        this.header=header;
    }
    public void setHeader(Object...objects)
    {
        this.header=new TableHeader().add(objects);
    }
    public TableRow returnAddRow()
    {
        TableRow row=new TableRow();
        this.tbody.addInner(row);
        return row;
    }
    public void addRow(TableRow row)
    {
        this.tbody.addInner(row);
    }
    public void addRow(tr row)
    {
        this.tbody.addInner(row);
    }
    public void addRowInline(Object...objects)
    {
        this.tbody.addInner(new TableRow().add(objects));
    }
    public void setFooter(TableFooter footer)
    {
        this.footer=footer;
    }
    public void setFooterInline(Object...objects)
    {
        this.footer=new TableFooter().add(objects);
    }
    
    public tbody tbody()
    {
        return this.tbody;
    }
    public TableHeader header()
    {
        return this.header;
    }
    public TableFooter footer()
    {
        return this.footer;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        //sb.append("$(document).ready(function(){$('#").append(id()).append("').DataTable(");

        String function="f_"+id();
        sb.append("$(document).ready("+function+");function "+function+"(){$('#").append(id()).append("').DataTable(");
                
        boolean commaNeeded=false;
//        sb.append('{');
    //    if (this.options==null)
        {
            ObjectBuilder ob=new ObjectBuilder();
            ob.add(this.options);
            sb.append(ob.toString());
        }
//        sb.append(");});");
        sb.append(");}");

        this.addInner(this.header);
        this.addInner(this.tbody);
        this.addInner(this.footer);

        

        if (sb.length()>0)
        {
            script script=new script().addInner(sb.toString());
            composer.getStringBuilder().append(script.toString());
        }
        //this.compose(composer);
        super.compose(composer);
    }

    
}
