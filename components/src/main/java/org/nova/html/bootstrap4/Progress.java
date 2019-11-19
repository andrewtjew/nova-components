package org.nova.html.bootstrap4;

public class Progress extends StyleComponent<Progress>
{
    public Progress()
    {
        super("div","progress");
    }
    
    public Progress width(int widthPercentage)
    {
        addInner(new ProgressBar().width(widthPercentage));
        return this;
    }
    
}
