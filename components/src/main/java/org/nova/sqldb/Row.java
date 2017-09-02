package org.nova.sqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;

public class Row
{
	final HashMap<String,Integer> mappings;
	final Object[] data;
	Row(HashMap<String,Integer> mappings,Object[] data)
	{
		this.mappings=mappings;
		this.data=data;
	}
	
	//get using SQL types
	public String getCHAR(int columnIndex)
	{
		return (String)this.data[columnIndex];
	}
	public String getCHAR(String columnName)
	{
		return getCHAR(this.mappings.get(columnName));
	}
	public String getVARCHAR(int columnIndex)
	{
		return (String)this.data[columnIndex];
	}
	public String getVARCHAR(String columnName)
	{
		return getVARCHAR(this.mappings.get(columnName));
	}
	public String getLONGVARCHAR(int columnIndex)
	{
		return (String)this.data[columnIndex];
	}
	public String getLONGVARCHAR(String columnName)
	{
		return getLONGVARCHAR(this.mappings.get(columnName));
	}
	public BigDecimal getNUMERIC(int columnIndex)
	{
		return (BigDecimal)this.data[columnIndex];
	}
	public BigDecimal getNUMERIC(String columnName)
	{
		return getNUMERIC(this.mappings.get(columnName));
	}
	public BigDecimal getDECIMAL(int columnIndex)
	{
		return (BigDecimal)this.data[columnIndex];
	}
	public BigDecimal getDECIMAL(String columnName)
	{
		return getDECIMAL(this.mappings.get(columnName));
	}
	public short getTINYINT(int columnIndex)
	{
		return (short)this.data[columnIndex];
	}
	public short getTINYINT(String columnName)
	{
		return getTINYINT(this.mappings.get(columnName));
	}
    public Short getNullableTINYINT(int columnIndex)
    {
        return (Short)this.data[columnIndex];
    }
    public Short getNullableTINYINT(String columnName)
    {
        return getNullableTINYINT(this.mappings.get(columnName));
    }
	public short getSMALLINT(int columnIndex)
	{
		return (short)this.data[columnIndex];
	}
	public short getSMALLINT(String columnName)
	{
		return getSMALLINT(this.mappings.get(columnName));
	}
    public Short getNullableSMALLINT(int columnIndex)
    {
        return (Short)this.data[columnIndex];
    }
    public Short getNullableSMALLINT(String columnName)
    {
        return getNullableSMALLINT(this.mappings.get(columnName));
    }
	public int getINTEGER(int columnIndex)
	{
		return (int)this.data[columnIndex];
	}
	public int getINTEGER(String columnName)
	{
		return getINTEGER(this.mappings.get(columnName));
	}
    public Integer getNullableINTEGER(int columnIndex)
    {
        return (Integer)this.data[columnIndex];
    }
    public Integer getNullableINTEGER(String columnName)
    {
        return getNullableINTEGER(this.mappings.get(columnName));
    }
	public long getBIGINT(int columnIndex)
	{
		return (long)this.data[columnIndex];
	}
	public long getBIGINT(String columnName)
	{
		return getBIGINT(this.mappings.get(columnName));
	}
    public Long getNullableBIGINT(int columnIndex)
    {
        return (Long)this.data[columnIndex];
    }
    public Long getNullableBIGINT(String columnName)
    {
        return getNullableBIGINT(this.mappings.get(columnName));
    }
	public float getREAL(int columnIndex)
	{
		return (float)this.data[columnIndex];
	}
	public float getREAL(String columnName)
	{
		return getREAL(this.mappings.get(columnName));
	}
    public Float getNullableREAL(int columnIndex)
    {
        return (Float)this.data[columnIndex];
    }
    public Float getNullableREAL(String columnName)
    {
        return getNullableREAL(this.mappings.get(columnName));
    }
	public double getFLOAT(int columnIndex)
	{
		return (double)this.data[columnIndex];
	}
	public double getFLOAT(String columnName)
	{
		return getFLOAT(this.mappings.get(columnName));
	}
    public Double getNullableFLOAT(int columnIndex)
    {
        return (Double)this.data[columnIndex];
    }
    public Double getNullableFLOAT(String columnName)
    {
        return getNullableFLOAT(this.mappings.get(columnName));
    }
	public double getDOUBLE(int columnIndex)
	{
		return (double)this.data[columnIndex];
	}
	public double getDOUBLE(String columnName)
	{
		return getDOUBLE(this.mappings.get(columnName));
	}
    public Double getNullableDOUBLE(int columnIndex)
    {
        return (Double)this.data[columnIndex];
    }
    public Double getNullableDOUBLE(String columnName)
    {
        return getNullableDOUBLE(this.mappings.get(columnName));
    }
	public byte[] getBINARY(int columnIndex)
	{
		return (byte[])this.data[columnIndex];
	}
	public byte[] getBINARY(String columnName)
	{
		return getBINARY(this.mappings.get(columnName));
	}
	public byte[] getVARBINARY(int columnIndex)
	{
		return (byte[])this.data[columnIndex];
	}
	public byte[] getVARBINARY(String columnName)
	{
		return getVARBINARY(this.mappings.get(columnName));
	}
	public byte[] getLONGVARBINARY(int columnIndex)
	{
		return (byte[])this.data[columnIndex];
	}
	public byte[] getLONGVARBINARY(String columnName)
	{
		return getLONGVARBINARY(this.mappings.get(columnName));
	}
	public Date getDATE(int columnIndex)
	{
		return (Date)this.data[columnIndex];
	}
	public Date getDATE(String columnName)
	{
		return getDATE(this.mappings.get(columnName));
	}
	public Time getTIME(int columnIndex)
	{
		return (Time)this.data[columnIndex];
	}
	public Time getTIME(String columnName)
	{
		return getTIME(this.mappings.get(columnName));
	}
	public Timestamp getTIMESTAMP(int columnIndex)
	{
		return (Timestamp)this.data[columnIndex];
	}
    public Timestamp getTIMESTAMP(String columnName)
    {
        return getTIMESTAMP(this.mappings.get(columnName));
    }
    public boolean getBIT(int columnIndex)
    {
        return (boolean)this.data[columnIndex];
    }
    public boolean getBIT(String columnName)
    {
        return getBIT(this.mappings.get(columnName));
    }
    public Boolean getNullableBIT(int columnIndex)
    {
        return (Boolean)this.data[columnIndex];
    }
    public Boolean getNullableBIT(String columnName)
    {
        return getNullableBIT(this.mappings.get(columnName));
    }

	@SuppressWarnings("unchecked")
	public <TYPE> TYPE get(int columnIndex)
	{
		return (TYPE)this.data[columnIndex];
	}
	public <TYPE> TYPE get(String columnName)
	{
		return get(this.mappings.get(columnName));
	}
	public Object[] getObjects()
	{
	    return this.data;
	}
	
}
