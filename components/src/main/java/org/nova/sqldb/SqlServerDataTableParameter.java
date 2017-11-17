package org.nova.sqldb;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

public class SqlServerDataTableParameter
{
    final private String name;
    final private SQLServerDataTable dataTable;
    
    public SqlServerDataTableParameter(String name,SQLServerDataTable dataTable)
    {
        this.name=name;
        this.dataTable=dataTable;
    }

    public String getName()
    {
        return name;
    }

    public SQLServerDataTable getDataTable()
    {
        return dataTable;
    }
}
