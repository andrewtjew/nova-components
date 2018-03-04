package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Responsiveness;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.table;
import org.nova.html.tags.tbody;
import org.nova.html.tags.div;

public class Table extends Element
{
    private final table table;
    private final tbody tbody;
    
    private boolean hover;
    private boolean striped;
    private boolean dark;
    private boolean bordered;
    private Responsiveness responsiveness;
    private boolean tableResponsive;
    private Responsiveness tableResponsiveness;
    
    public Table(TableHeading heading)
    {
       this.table=new table();
       if (heading!=null)
       {
           this.table.addInner(heading);
       }
       this.tbody=this.table.returnAddInner(new tbody());
    }

    public Table()
    {
        this(null);
    }

    public Table hover(boolean value)
    {
        this.hover=value;
        return this;
    }
    public Table striped(boolean value)
    {
        this.striped=value;
        return this;
    }
    public Table dark(boolean value)
    {
        this.dark=value;
        return this;
    }
    public Table bordered(boolean value)
    {
        this.bordered=value;
        return this;
    }
    public Table responsiveness(Responsiveness value)
    {
        this.responsiveness=value;
        return this;
    }
    public Table tableResponsiveness(Responsiveness value)
    {
        this.tableResponsiveness=value;
        return this;
    }
    public Table tableResponsive(boolean value)
    {
        this.tableResponsive=value;
        return this;
    }
    
    public Table add(TableRow row)
    {
        this.tbody.addInner(row);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder tableClass=new ClassBuilder("table");
        tableClass.addIf(this.hover,"table-hover");
        tableClass.addIf(this.striped,"table-striped");
        tableClass.addIf(this.dark,"table-dark");
        tableClass.addIf(this.bordered,"table-bordered");
        tableClass.addIf(this.responsiveness!=null,"table",this.responsiveness);
        tableClass.applyTo(this.table);
        
        div div=new div();
        
        if ((this.tableResponsive)||(this.tableResponsiveness!=null))
        {
            ClassBuilder divClass=new ClassBuilder("class");
            divClass.addFragments("responsive");
            divClass.addFragments(this.tableResponsiveness.toString());
            divClass.applyTo(div);
        }
        div.addInner(this.table);
        composer.render(div);
        
    }
    
}
