package org.nova.net.printing;

public class SerializedCommand
{
    private final Code code;
    private final String text;
    
    public SerializedCommand(Code code,String text)
    {
        this.code=code;
        this.text=text;
    }
}
