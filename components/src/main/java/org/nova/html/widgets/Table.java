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
    
    public Table setHeadRow(tr tr)
    {
        this.thead.setInner(tr);
        return this;
    }
    public Table setHeadRowItems(Object...objects)
    {
        this.thead.addInner(new Row().add(objects));
        return this;
    }
    public Table setFootRow(tr tr)
    {
        this.tfoot.setInner(tr);
        return this;
    }
    public Table setFootRowItems(Object...objects)
    {
        this.tfoot.addInner(new Row().add(objects));
        return this;
    }
    public Table addBodyRow(tr tr)
    {
        this.tbody.addInner(tr);
        return this;
    }
    public Table addBodyRowItems(Object...objects)
    {
        this.tbody.addInner(new Row().add(objects));
        return this;
    }
    
}
