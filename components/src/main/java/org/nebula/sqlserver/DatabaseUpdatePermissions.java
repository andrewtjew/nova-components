package org.nebula.sqlserver;

public class DatabaseUpdatePermissions
{
    public boolean deleteTable=false;
    public boolean deleteColumn=false;
    public boolean createTable=true;
    public boolean createNullableTableColumn=true;
    public boolean createNotNullableTableColumn=false;
    public boolean alterColumnToLargerSize=true;
    public boolean alterColumnToSmallerSize=false;
    public boolean alterColumnToNullable=true;
    public boolean alterColumnToNotNullable=false;
    public boolean alterIdentityColumns=false;
    public boolean alterIdentityStart=false;
    public boolean alterIdentityIncrement=false;
    public boolean deleteFunction=true;
    public boolean createFunction=true;
    public boolean replaceFunction=true;

    public boolean deleteProcedure=false; 
    public boolean createProcedure=true;
    public boolean replaceProcedure=true;
}
