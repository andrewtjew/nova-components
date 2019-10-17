package org.nova.html.DataTables;

import org.nova.html.deprecated.ObjectBuilder;
import org.nova.html.deprecated.TableFooter;
import org.nova.html.deprecated.TableHeader;
import org.nova.html.deprecated.TableRow;
import org.nova.html.elements.Composer;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.tbody;
import org.nova.html.tags.tr;

//!!! Requires jquery

public class DataTable1_10 extends GlobalEventTagElement<DataTable1_10>
{
    final private tbody tbody;
    final private DataTableOptions options;

    private TableHeader header;
    private TableFooter footer;

    public DataTable1_10(Head head,DataTableOptions options)
    {
        this(head,options,"https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css","https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js");
    }

    public DataTable1_10(DataTableOptions options)
    {
        this(null,options);
    }
    
    public DataTable1_10(Head head,DataTableOptions options,String cssFilePath,String scriptFilePath)
    {
        super("table");
        this.addClass("display");
        this.tbody=new tbody();
        this.options=options;

        if (head!=null)
        {
            String key=DataTable1_10.class.getCanonicalName();
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
    public void setHeaderInline(Object...objects)
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

    @Override
    public void compose(Composer composer) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("$(document).ready(function(){$('#").append(id()).append("').DataTable(");
        boolean commaNeeded=false;
//        sb.append('{');
    //    if (this.options==null)
        {
            ObjectBuilder ob=new ObjectBuilder();
            ob.add(this.options);
            sb.append(ob.toString());
        }
  //      sb.append('}');
        sb.append(");});");

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
