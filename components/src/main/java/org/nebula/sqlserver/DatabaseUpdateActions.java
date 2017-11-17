package org.nebula.sqlserver;

public class DatabaseUpdateActions
{
    public int deleteTable=0;
    public int deleteColumn=0;
    public int createTable=0;
    public int createNullableTableColumn=0;
    public int createNotNullableTableColumn=0;
    public int alterColumnToLargerSize=0;
    public int alterColumnToSmallerSize=0;
    public int alterColumnToNullable=0;
    public int alterColumnToNotNullable=0;
    public int alterIdentityColumns=0;
    public int alterIdentityStart=0;
    public int alterIdentityIncrement=0;
    public int deleteFunction=0;
    public int createFunction=0;
    public int replaceFunction=0;

    public int deleteProcedure=0; 
    public int createProcedure=0;
    public int replaceProcedure=0;
}
