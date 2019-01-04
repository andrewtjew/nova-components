package org.nova.html.bootstrap4;

public class TabPane extends StyleComponent<TabPane>
{
    public TabPane()
    {
        super("div","tab-pane");
    }
    
    public TabPane fade()
    {
        addClass("fade");
        return this;
    }

    public TabPane show()
    {
        addClass("show");
        return this;
    }
    
    public TabPane active()
    {
        addClass("active");
        return this;
    }
}
