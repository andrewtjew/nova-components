package org.nova.html.bootstrap4;

public class ProgressBar extends StyleComponent<ProgressBar>
{
    public ProgressBar()
    {
        super("div","progress-bar");
        attr("role","progressbar");
    }
    
    public ProgressBar width(int percentage)
    {
        attr("style","width:"+percentage+"%");
        return this;
    }
    
    public ProgressBar striped()
    {
        addClass(this.getComponentClass(),"striped");
        return this;
    }
    public ProgressBar animated()
    {
        addClass(this.getComponentClass(),"animated");
        return this;
    }
}
