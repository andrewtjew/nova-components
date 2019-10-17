package org.nova.sqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class GeneratedKeys
{
    private final List<Object> keys;
    GeneratedKeys(List<Object> keys)
    {
        this.keys=keys;
    }
    @SuppressWarnings("unchecked")
    public <TYPE> TYPE get(int index)
    {
        return (TYPE)this.keys.get(index);
    }
    public BigDecimal getBigDecimal(int index)
    {
        return (BigDecimal)this.keys.get(index);
    }
    public BigInteger getBigInteger(int index)
    {
        return (BigInteger)this.keys.get(index);
    }
    public long getLong(int index)
    {
        return (long)this.keys.get(index);
    }
    public int getInt(int index)
    {
        return (int)this.keys.get(index);
    }
    public long getAsLong(int index) throws Exception
    {
        Object key=this.keys.get(index);
        if (key instanceof Long)
        {
            return (long)key;
        }
        else if (key instanceof BigInteger)
        {
            return ((BigInteger)key).longValue();
        }
        else if (key instanceof BigDecimal)
        {
            return ((BigDecimal)key).longValue();
        }
        else if (key instanceof Integer)
        {
            return (Integer)key;
        }
        if (key!=null)
        {
            throw new Exception("class="+key.getClass().getName());
        }
        throw new Exception();
    }
    public int size()
    {
        return keys.size();
    }
}
