/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.html.bootstrap4.DataTables;

import java.util.ArrayList;

import org.nova.html.DataTables.ColumnDef;
import org.nova.html.DataTables.DataTable1_10;
import org.nova.html.DataTables.DataTableColumnOrder;
import org.nova.html.DataTables.DataTableOptions;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.TableRow;
import org.nova.html.deprecated.ObjectBuilder;
import org.nova.html.deprecated.TableFooter;
import org.nova.html.deprecated.TableHeader;
import org.nova.html.elements.Composer;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.tbody;
import org.nova.html.tags.tr;
import org.nova.utils.TypeUtils;

//!!! Requires jquery

public class DataTableBootstrap4 extends StyleComponent<DataTableBootstrap4>
{
    final private tbody tbody;
    private DataTableOptions options;

    private TableHeader header;
    private TableFooter footer;
    
    
    public DataTableBootstrap4(Head head,DataTableOptions options)
    {
        super("table","table");
        this.tbody=new tbody();
        this.options=options;

        if (head!=null)
        {
            String key=DataTableBootstrap4.class.getCanonicalName();
            head.add(key,new link().rel(link_rel.stylesheet).type("text/css").href("https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"));
        }
    }
    
    
    public DataTableBootstrap4(DataTableOptions options)
    {
        this(null,options);
    }
    
    public DataTableBootstrap4 cell_border()
    {
        addClass("cell-border");
        return this;
    }

    public DataTableBootstrap4 compact()
    {
        addClass("compact");
        return this;
    }

    public DataTableBootstrap4 hover()
    {
        addClass("hover");
        return this;
    }
    
    public DataTableBootstrap4 order_column()
    {
        addClass("order-column");
        return this;
    }
    
    public DataTableBootstrap4 row_border()
    {
        addClass("row-border");
        return this;
    }
    
    public DataTableBootstrap4 stripe()
    {
        addClass("stripe");
        return this;
    }
    
//    public DataTableBootstrap4 responsive()
//    {
//        addClass("dt-responsive");
//        return this;
//    }
    
    public DataTableBootstrap4 setHeader(TableHeader header)
    {
        this.header=header;
        return this;
    }
    public DataTableBootstrap4 setHeader(Object...objects)
    {
        this.header=new TableHeader().add(objects);
        return this;
    }
    public DataTableBootstrap4 setHeaderWithBlankColumnsNotOrderable(Object...objects)
    {
        if (this.options==null)
        {
            this.options=new DataTableOptions();
        }
        if (this.options.columnDefs==null)
        {
            ArrayList<Integer> list=new ArrayList<>();
            for (int i=0;i<objects.length;i++)
            {
                Object object=objects[i];
                if (object==null)
                {
                    list.add(i);
                }
                else if (object instanceof String)
                {
                    if (((String)object).length()==0)
                    {
                        list.add(i);
                    }
                }
            }
            if (list.size()>0)
            {
                ColumnDef columnDef=new ColumnDef(TypeUtils.intListToArray(list));
                columnDef.orderable=false;
                columnDef.searchable=false;
                this.options.columnDefs=new ColumnDef[]{columnDef}; 
            }
        }
        
        this.header=new TableHeader().add(objects);
        return this;
    }
    public TableRow returnAddRow()
    {
        TableRow row=new TableRow();
        this.tbody.addInner(row);
        return row;
    }
    public DataTableBootstrap4 addRow(TableRow row)
    {
        this.tbody.addInner(row);
        return this;
    }
    public DataTableBootstrap4 addRow(tr row)
    {
        this.tbody.addInner(row);
        return this;
    }
    public DataTableBootstrap4 addRow(Object...objects)
    {
        this.tbody.addInner(new TableRow().add(objects));
        return this;
    }
    public DataTableBootstrap4 setFooter(TableFooter footer)
    {
        this.footer=footer;
        return this;
    }
    public DataTableBootstrap4 setFooter(Object...objects)
    {
        this.footer=new TableFooter().add(objects);
        return this;
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
      sb.append("$(document).ready(function(){$('#").append(id()).append("').DataTable(");
                
        {
            ObjectBuilder ob=new ObjectBuilder();
            ob.add(this.options);
            sb.append(ob.toString());
        }
        sb.append(");});");

        this.addInner(this.header);
        this.addInner(this.tbody);
        this.addInner(this.footer);

        

        if (sb.length()>0)
        {
            script script=new script().addInner(sb.toString());
            composer.getStringBuilder().append(script.getHtml());
        }
        //this.compose(composer);
        super.compose(composer);
    }

    
}
