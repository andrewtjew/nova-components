package org.nova.html.operator;

import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;
import org.nova.html.widgets.Table;


public class TwoColumnTable extends Table
{
    final private td seperator;
    
    public TwoColumnTable(String seperator)
    {
        this.seperator=seperator!=null?new td().addInner(seperator):null;
    }
    
    public void addItems(Element first,Element second)
    {
        this.tbody().addInner(new tr().addInners(new td().addInner(first),this.seperator,new td().addInner(second)));
    }
    public void addItems(Object first,Element second)
    {
        this.tbody().addInner(new tr().addInners(new td().addInner(first),this.seperator,new td().addInner(second)));
    }
    public void addItems(Element first,Object second)
    {
        this.tbody().addInner(new tr().addInners(new td().addInner(first),this.seperator,new td().addInner(second)));
    }
    public void addItems(Object first,Object second)
    {
        this.tbody().addInner(new tr().addInners(new td().addInner(first),this.seperator,new td().addInner(second)));
    }
    public void addSecond(Element second,boolean noSeperator)
    {
        tr tr=new tr().addInner(new td());
        if (this.seperator!=null)
        {
            if (noSeperator)
            {
                tr.addInner(new td());
            }
            else
            {
                tr.addInner(this.seperator);
            }
        }
        tr.addInner(new td().addInner(second));
        this.tbody().addInner(tr);
    }
    public void addFirstItem(Element first,boolean noSeperator)
    {
        tr tr=new tr().addInner(new td().addInner(first));
        if (this.seperator!=null)
        {
            if (noSeperator)
            {
                tr.addInner(new td());
            }
            else
            {
                tr.addInner(this.seperator);
            }
        }
        tr.addInner(new td());
        this.tbody().addInner(tr);
    }
    

}
