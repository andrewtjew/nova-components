package org.nebula.sqlserver;

public class SqlServerObjects
{
    final private Function[] functions; 
    final private Procedure[] procedures; 
    final private Table[] tables ;
    
    SqlServerObjects(Function[] functions,Procedure[] procedures,Table[] tables)
    {
        this.functions=functions;
        this.procedures=procedures;
        this.tables=tables;
    }

    public Function[] getFunctions()
    {
        return functions;
    }

    public Procedure[] getProcedures()
    {
        return procedures;
    }

    public Table[] getTables()
    {
        return tables;
    }
    
}
