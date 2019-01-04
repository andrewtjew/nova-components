package org.nova.html.bootstrap4;

public class Progress extends StyleComponent<Progress>
{
    public Progress()
    {
        super("div","progress");
    }
    
    public Progress setProgress(int widthPercentage)
    {
        addInner(new ProgressBar().setWidth(widthPercentage));
        return this;
    }
    
}
