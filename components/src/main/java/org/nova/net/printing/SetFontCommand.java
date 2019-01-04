package org.nova.net.printing;

public class SetFontCommand extends Command
{ 
    private final String name; 
    private final float size; 
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;
    
    public SetFontCommand(String name,float size,boolean bold, boolean italic, boolean underline)
    {
        super(Code.SetFont);
        this.name=name;
        this.size=size;
        this.bold=bold;
        this.italic=italic;
        this.underline=underline;
    }
    public SetFontCommand(String name,float size)
    {
        this(name,size,false,false,false);
    }
}
