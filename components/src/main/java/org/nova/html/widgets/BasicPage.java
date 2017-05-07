package org.nova.html.widgets;
import org.nova.html.tags.html;
import org.nova.html.tags.head;
import org.nova.html.tags.body;

public class BasicPage extends Content
{
    final private html html;
    final private head head;
    final private body body;
    
    public BasicPage(String docType)
    {
        this.addInner(new DocType(docType));
        this.html=this.returnAddInner(new html());
        this.head=this.html.returnAddInner(new head());
        this.body=this.html.returnAddInner(new body());
    }
    public BasicPage()
    {
        this("html");
    }
    
    public head head()
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
