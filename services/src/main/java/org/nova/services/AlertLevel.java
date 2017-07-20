package org.nova.services;

public enum AlertLevel
{
    
    APPLICATION_WARNING(0),
    APPLICATION_ERROR(1),
    APPLICATION_CRITICAL(2),
    SYSTEM_WARNING(3),
    SYSTEM_ERROR(4),
    SYSTEM_CRITICAL(5),
    ;
    private int value;
    
    AlertLevel(int value)
    {
        this.value=value;
    }
    
    public int getValue()
    {
        return this.value;
    }

    public static AlertLevel fromValue(int value)
    {
        for (AlertLevel level:AlertLevel.values())
        {
            if (level.getValue()==value)
            {
                return level;
            }
        }
        return null;
    }

    static AlertLevel HIGHEST_LEVEL=AlertLevel.SYSTEM_CRITICAL;
    
}
