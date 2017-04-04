package org.nova.json;

public class JSONEnum<ENUM extends Enum<?>>
{
    public ENUM value;
    public JSONEnum(ENUM value)
    {
        this.value=value;
    }
    public JSONEnum()
    {
    }
}
