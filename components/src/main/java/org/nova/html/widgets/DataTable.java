package org.nova.html.widgets;

import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.nova.core.Utils;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.table;
import org.nova.html.tags.tbody;
import org.nova.html.tags.tfoot;
import org.nova.html.tags.th;
import org.nova.html.tags.thead;
import org.nova.html.tags.tr;

public class DataTable extends Element
{
    final private String id;
    final private String class_;
    private Element headers=null;
    private Element footers=null;
    private ListSequence rows;
    private int[] lengthMenu;

    public DataTable(String id,String class_)
    {
        this.id=id;
        this.class_=class_;
        this.rows=new ListSequence();
    }
    public DataTable(String id)
    {
        this(id,null);
    }
    public DataTable headerRow(tr headers)
    {
        this.headers=headers;
        return this;
    }
    public DataTable headerRow(Row headers)
    {
        this.headers=headers;
        return this;
    }
    public DataTable footerRow(tr footers)
    {
        this.footers=footers;
        return this;
    }
    public DataTable footerRow(Row footers)
    {
        this.footers=footers;
        return this;
    }

    public DataTable lengthMenu(int...lengths)
    {
        this.lengthMenu=lengths;
        return this;
    }
    public DataTable row(tr row)
    {
        this.rows.add(row);
        return this;
    }
    public DataTable row(Row row)
    {
        this.rows.add(row);
        return this;
    }
    
    private Element build()
    {
        ListSequence parts=new ListSequence();
        if (this.headers!=null)
        {
            parts.add(new thead().inner(this.headers));
        }
        if (this.footers!=null)
        {
            parts.add(new tfoot().inner(this.footers));
        }
        parts.add(new tbody().inner(this.rows));

        ArraySequence dataTable=new ArraySequence(4);
        dataTable.set(0, new script().src("/resources/html/DataTables/datatables.min.js"));
        StringBuilder sb=new StringBuilder();
        sb.append("$(document).ready(function(){$('#").append(this.id).append("').DataTable(");
        if ((this.lengthMenu!=null)&&(this.lengthMenu.length>0))
        {
            ArrayList<String> labels=new ArrayList<>();
            for (int i=0;i<this.lengthMenu.length;i++)
            {
                if (this.lengthMenu[i]>=0)
                {
                    labels.add(Integer.toString(this.lengthMenu[i]));
                }
                else
                {
                    labels.add("'All'");
                }
            }
            sb.append("{'lengthMenu': [["+Utils.combine(Utils.toList(this.lengthMenu), ",")+"], ["+Utils.combine(labels, ",")+"]]}");
        }
        sb.append(");} );");
        
        dataTable.set(1, new script().inner(sb.toString()));
        dataTable.set(2, new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/DataTables/datatables.min.css"));
        dataTable.set(3, new table().id(this.id).class_(this.class_).inner(parts));
        return dataTable;
        
    }
    
    @Override
    public String toString()
    {
        return build().toString();
    }
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        build().write(outputStream);
    }

    
}
