package org.nova.html.widgets;

import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.widgets.w3c.TopNavigationMenu;

public class Panel extends div
{
    final private div content;
    final private div heading;
    public Panel(Head head,String id,String sourcePath,String cssFile,String heading)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        this.heading=returnAddInner(new div().class_("panel-heading").addInner(heading));
        this.content=returnAddInner(new div().class_("panel-content"));
        id(id).class_("panel");
        if (head!=null)
        {
            head.add(TopNavigationMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
    }
    public Panel(Head head,String id,String heading)
    {
        this(head,id, "/resources/html","/widgets/Panel/style.css",heading);
    }
    public Panel(Head head,String heading)
    {
        this(head,null,heading);
    }
    public Panel(String heading)
    {
        this(null,heading);
    }

    public div content()
    {
        return this.content;
    }
    public div heading()
    {
        return this.heading;
    }
}
