package org.nova.html.bootstrap4;

import org.nova.html.tags.tbody;

public class Table extends StyleComponent<Table>
{
    private final tbody tbody;
    
    public Table(TableHeading heading)
    {
       super("table","table");
       if (heading!=null)
       {
           addInner(heading);
       }
       this.tbody=returnAddInner(new tbody());
    }

    public Table()
    {
        this(null);
    }

    public Table hover()
    {
        addClass("table-hover");
        return this;
    }
    public Table striped()
    {
        addClass("table-striped");
        return this;
    }
    public Table bordered()
    {
        addClass("table-bordered");
        return this;
    }
    public Table responsive()
    {
        addClass("table-responsive");
        return this;
    }

    public Table addRow(TableRow row)
    {
        this.tbody.addInner(row);
        return this;
    }
    public Table addRowInline(Object...objects)
    {
        this.tbody.addInner(new TableRow().add(objects));
        return this;
    }
    
    public TableRow returnAddRow()
    {
        TableRow tableRow=new TableRow();
        addRow(tableRow);
        return tableRow;
    }
    
}
