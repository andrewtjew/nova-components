package org.nova.sqldb;

import java.math.BigDecimal;
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
    public long getLong(int index)
    {
        return (long)this.keys.get(index);
    }
    public int getInt(int index)
    {
        return (int)this.keys.get(index);
    }
    public int size()
    {
        return keys.size();
    }
}
