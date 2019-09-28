package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.tbody;

public class Table extends StyleComponent<Table>
{
    private final tbody tbody;
    private boolean responsive=false;
    public Table(TableHeading heading)
    {
       super("table","table");
       if (heading!=null)
       {
           addInner(heading);
       }
       this.tbody=returnAddInner(new tbody());
    }

    public Table addHeading(TableHeading heading)
    {
        addInner(heading);
        return this;
    }
    public Table()
    {
        this(null);
    }

    public Table w_auto()
    {
        addClass("w-auto");
        return this;
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
        this.responsive=true;
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
    public tbody tbody()
    {
        return this.tbody;
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        
        if (this.responsive)
        {
            div group=new div().addClass("table-responsive");
            group.addInner(new Element()
            {
                @Override
                public void compose(Composer composer) throws Throwable
                {
                    composeThis(composer);
                }
            });
            group.compose(composer);
        }
        else
        {
            super.compose(composer);
        }
        
    }
    
}
