package org.nova.html.tags.ext;
import org.nova.html.tags.html;
import org.nova.html.deprecated.Content;
import org.nova.html.ext.DocType;
import org.nova.html.ext.Head;
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
