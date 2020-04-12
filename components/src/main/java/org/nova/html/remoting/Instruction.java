package org.nova.html.remoting;

public class Instruction
{
    public boolean trace;
    public String code;
    Instruction(String code,boolean trace)
    {
        this.code=code;
        this.trace=trace;
    }
}
