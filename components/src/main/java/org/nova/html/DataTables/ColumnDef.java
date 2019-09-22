package org.nova.html.DataTables;

public class ColumnDef extends Column
{ 
    Object targets;
    public ColumnDef(int...targets)
    {
        this.targets=targets;
    }
    public ColumnDef()
    {
        this.targets="_all";
    }
    
}
