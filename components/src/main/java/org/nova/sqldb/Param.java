package org.nova.sqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLType;
import java.sql.Types;

import org.nova.sqldb.Params.Type;

public class Param
{
	enum Direction
	{
		IN,
		OUT,
		IN_OUT,
	}
	
	final Object inValue;
	final Direction direction;
	final int sqlType;
	
	private Param(Direction direction,Object inValue) throws Exception
	{
		this.inValue=inValue;
		this.direction=direction;
		this.sqlType=getSqlOutType(inValue.getClass());
	}

	private Param(Direction direction,int outSqlType,Object inValue) throws Exception
	{
		this.inValue=inValue;
		this.direction=direction;
		this.sqlType=outSqlType;
	}

	private Param(Class<?> outType) throws Exception
	{
		this.inValue=null;
		this.direction=Direction.OUT;
		this.sqlType=getSqlOutType(outType);
	}

	private Param(int sqlType) throws Exception
	{
		this.inValue=null;
		this.direction=Direction.OUT;
		this.sqlType=sqlType;
	}
	
	static int getSqlOutType(Class<?> type) throws Exception
	{
		if (type==String.class)
		{
			return Types.VARCHAR;
		}
		else if ((type==Byte.class)||(type==byte.class))
		{
			return Types.TINYINT;
		}
		else if ((type==Short.class)||(type==short.class))
		{
			return Types.SMALLINT;
		}
		else if ((type==Integer.class)||(type==int.class))
		{
			return Types.INTEGER;
		}
		else if ((type==Long.class)||(type==long.class))
		{
			return Types.BIGINT;
		}
		else if ((type==Float.class)||(type==float.class))
		{
			return Types.REAL;
		}
		else if ((type==Double.class)||(type==double.class))
		{
			return Types.DOUBLE;
		}
		else if (type==byte[].class)
		{
			return Types.VARBINARY;
		}
		else if (type==java.sql.Date.class)
		{
			return Types.DATE;
		}
		else if (type==java.sql.Time.class)
		{
			return Types.TIME;
		}
		else if (type==java.sql.Timestamp.class)
		{
			return Types.TIMESTAMP;
		}
		else if (type==BigInteger.class)
		{
			return Types.DECIMAL;
		}
		else if (type==BigDecimal.class)
		{
			return Types.DECIMAL;
		}
		throw new Exception("Incompatible type. Name of outType: "+type.getCanonicalName());
	}
	
	public static Param In(Object inValue) throws Exception
	{
		return new Param(Direction.IN,inValue);
	}
	
	public static Param InOut(Object inValue) throws Exception
	{
		return new Param(Direction.IN_OUT,inValue);
	}
	
	public static Param InOut(int outSqlType,Object inValue) throws Exception
	{
		return new Param(Direction.IN_OUT,outSqlType,inValue);
	}

	public static Param InOut(Class<?> outType,Object inValue) throws Exception
	{
		return new Param(Direction.IN_OUT,getSqlOutType(outType),inValue);
	}
	
	public static Param Out(int sqlType) throws Exception
	{
		return new Param(sqlType);
	}

	public static Param Out(Class<?> type) throws Exception
	{
		return new Param(getSqlOutType(type));
	}

	public static Param Return(int sqlType) throws Exception
	{
		return new Param(sqlType);
	}

	public static Param Return(Class<?> type) throws Exception
	{
		return new Param(getSqlOutType(type));
	}
}
