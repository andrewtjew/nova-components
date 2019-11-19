/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.sqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;

public class Param
{
	public enum Direction
	{
		IN,
		OUT,
		IN_OUT,
	}
	
	public final Object inValue;
	public final int sqlType;
	public final Direction direction;
	
	
	private Param(Direction direction,int sqlType,Object inValue) throws Exception
	{
		this.inValue=inValue;
		this.direction=direction;
		this.sqlType=sqlType;
	}
    private static int getSqlTypeFromObject(Object object) throws Exception
    {
        if (object==null)
        {
            return Types.NULL;
        }
        return getSqlType(object.getClass());
    }
	public static int getSqlType(Class<?> type) throws Exception
	{
		if (type==String.class)
		{
			return Types.VARCHAR;
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
        else if ((type==Integer.class)||(type==int.class))
        {
            return Types.INTEGER;
        }
		else if (type==byte[].class)
		{
			return Types.VARBINARY;
		}
        else if (type==java.sql.Timestamp.class)
        {
            return Types.TIMESTAMP;
        }
		else if (type==java.sql.Date.class)
		{
			return Types.DATE;
		}
		else if (type==java.sql.Time.class)
		{
			return Types.TIME;
		}
        else if ((type==boolean.class)||(type==Boolean.class))
        {
            return Types.BIT;
        }
		else if (type==BigInteger.class)
		{
			return Types.BIGINT;
		}
		else if (type==BigDecimal.class)
		{
			return Types.DECIMAL;
		}
        else if ((type==Byte.class)||(type==byte.class))
        {
            return Types.TINYINT;
        }
        else if ((type==Short.class)||(type==short.class))
        {
            return Types.SMALLINT;
        }
		throw new Exception("Incompatible type. Name of outType: "+type.getCanonicalName());
	}
	
    public static Param In(Object inValue) throws Exception
    {
        return new Param(Direction.IN,getSqlTypeFromObject(inValue),inValue);
    }
    public static Param In(Class<?> type,Object inValue) throws Exception
    {
        int sqlType=getSqlType(type);
        if (inValue!=null)
        {
            if (type!=inValue.getClass())
            {
                throw new Exception("type is not type of inValue");
            }
        }
        return new Param(Direction.IN,sqlType,inValue);
    }
    public static Param In(int sqlType,Object inValue) throws Exception
    {
        return new Param(Direction.IN,sqlType,inValue);
    }
	public static Param InOut(Object inValue) throws Exception
	{
		return new Param(Direction.IN_OUT,getSqlTypeFromObject(inValue),inValue);
	}
    public static Param InOut(Class<?> type,Object inValue) throws Exception
    {
        int sqlType=getSqlType(type);
        if (inValue!=null)
        {
            if (type!=inValue.getClass())
            {
                throw new Exception("type is not type of inValue");
            }
        }
        return new Param(Direction.IN_OUT,sqlType,inValue);
    }
    public static Param Object(Object inValue) throws Exception
    {
        return new Param(null,0,inValue);
    }
	
    public static Param Out(Class<?> type) throws Exception
    {
        return new Param(Direction.OUT,getSqlType(type),null);
    }
    public static Param Out(int sqlType) throws Exception
    {
        return new Param(Direction.OUT,sqlType,null);
    }
    
    public static Param[] build(Param...params)
    {
        return params;
    }
    
}
