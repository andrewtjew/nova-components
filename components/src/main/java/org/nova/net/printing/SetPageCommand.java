package org.nova.net.printing;

public class SetPageCommand extends Command
{ 
    private final boolean landscape;
    
    public SetPageCommand(boolean landscape)
    {
        super(Code.SetPage);
        this.landscape=landscape;
    }
}
