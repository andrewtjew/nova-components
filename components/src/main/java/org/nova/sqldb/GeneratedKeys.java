package org.nova.sqldb;

import java.math.BigDecimal;
import java.util.List;

public class GeneratedKeys
{
    private final List<BigDecimal> keys;
    GeneratedKeys(List<BigDecimal> keys)
    {
        this.keys=keys;
    }
    public BigDecimal get(int index)
    {
        return this.keys.get(index);
    }
}
