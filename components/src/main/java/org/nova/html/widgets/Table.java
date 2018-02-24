package org.nova.html.widgets;

import org.nova.html.tags.table;
import org.nova.html.tags.thead;
import org.nova.html.tags.tr;
import org.nova.html.tags.tbody;
import org.nova.html.tags.tfoot;
import org.nova.html.tags.th;

public class Table extends table
{
    final private thead thead;
    final private tfoot tfoot;
    final private tbody tbody;
    
    public Table()
    {
        this.thead=returnAddInner(new thead()); 
        this.tfoot=returnAddInner(new tfoot()); 
        this.tbody=returnAddInner(new tbody()); 
    }

    public thead thead()
    {
        return thead;
    }

    public tfoot tfoot()
    {
        return tfoot;
    }

    public tbody tbody()
    {
        return tbody;
    }
    
    public void setHeadRow(Row row)
    {
        this.thead.setInner(row);
    }
    public void setHeadRowItems(Object...objects)
    {
        this.thead.addInner(new Row().add(objects));
    }
    public void setFootRow(Row row)
    {
        this.tfoot.setInner(row);
    }
    public void setFootRowItems(Object...objects)
    {
        this.tfoot.addInner(new Row().add(objects));
    }
    public void addBodyRow(Row row)
    {
        this.tbody.addInner(row);
    }
    public void addBodyRowItems(Object...objects)
    {
        this.tbody.addInner(new Row().add(objects));
    }
    
}
