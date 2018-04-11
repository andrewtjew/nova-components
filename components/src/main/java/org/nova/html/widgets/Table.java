package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.link;
import org.nova.html.tags.table;
import org.nova.html.tags.tbody;

public class Table extends Element
{
    final private table table;
    final private tbody tbody;
    private TableHeader header;
    private TableFooter footer;
    
    public Table()
    {
        this(null,"Table",null);
    }
    
    public Table(Head head)
    {
        this(head,"Table",DEFAULT_CSS_FILE_PATH);
    }
    
    private static String DEFAULT_CSS_FILE_PATH="/resources/html/widgets/Table/style.css";
    
    static public link DEFAULT_CSS_LINK()
    {
        return new link().rel(link_rel.stylesheet).type("text/css").href(DEFAULT_CSS_FILE_PATH);
    }
    
    
    public Table(Head head,String class_,String cssFilePath)
    {
        this.table=new table();
        this.tbody=new tbody();

        this.table.class_(class_);
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(cssFilePath);
        if (head!=null)
        {
            head.add(class_,link);
        }
        else
        {
            this.table().addInner(link);
        }
    }
    
    public table table()
    {
        return this.table;
    }
    
    public tbody tbody()
    {
        return this.tbody;
    }
    
    public void setHeader(TableHeader header)
    {
        this.header=header;
    }
    public void setHeaderInline(Object...objects)
    {
        this.header=new TableHeader().add(objects);
    }
    public void addRow(TableRow row)
    {
        this.tbody.addInner(row);
    }
    
    public void addRowInline(Object...objects)
    {
        this.tbody.addInner(new TableRow().add(objects));
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.table.addInner(this.header);
        this.table.addInner(this.tbody);
        this.table.addInner(this.footer);
        composer.render(this.table);
    }
}
