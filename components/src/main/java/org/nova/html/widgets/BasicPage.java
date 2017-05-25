package org.nova.html.widgets;
import org.nova.html.tags.html;
import org.nova.html.tags.link;
import org.nova.html.tags.meta;
import org.nova.html.tags.head;

import java.io.OutputStream;

import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.enums.name;
import org.nova.html.tags.body;

public class BasicPage extends Content
{
    final private html html;
    final private Head head;
    final private body body;
    
    public BasicPage(String docType)
    {
        addInner(new DocType(docType));
        this.html=returnAddInner(new html());
        this.head=this.html.returnAddInner(new Head());
//        this.head.addInner(new meta().name(name.viewport).content("width=device-width,initial-scale=1.0"));
        this.body=this.html.returnAddInner(new body());
    }
    public BasicPage()
    {
        this("html");
    }
    
    public Head head()
    {
        return this.head;
    }
    public body body()
    {
        return this.body;
    }
    public html html()
    {
        return this.html;
    }
    
}
