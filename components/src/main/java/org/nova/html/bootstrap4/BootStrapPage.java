package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.character_set;
import org.nova.html.enums.http_equiv;
import org.nova.html.enums.link_rel;
import org.nova.html.enums.name;
import org.nova.html.ext.Content;
import org.nova.html.ext.DocType;
import org.nova.html.ext.Head;
import org.nova.html.tags.body;
import org.nova.html.tags.html;
import org.nova.html.tags.link;
import org.nova.html.tags.meta;
import org.nova.html.tags.script;

public class BootStrapPage extends Element
{
    final private Head head;
    final private body body;
    final private Content content;
    public BootStrapPage(String title,String lang,String compatible)
	{
        this.content=new Content();
        this.content.addInner(new DocType("html"));
    	html html=this.content.returnAddInner(new html().lang(lang));
    	
    	this.head=html.returnAddInner(new Head());
    	if (title!=null)
    	{
    	    this.head.title(title);
    	}
    	if (compatible!=null)
    	{
    	    this.head().addInner(new meta().http_equiv_content(http_equiv.X_UA_compatible,compatible));
    	}
    	this.head.addInner(new meta().name(name.viewport).content("width=device-width, initial-scale=1, shrink-to-fit=no"));
    	this.head.addInner(new meta().charset(character_set.UTF_8));
        this.head.addInner(new link().rel(link_rel.stylesheet).href("https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"));
        this.head.addInner(new script().src("https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"));
        this.head.addInner(new script().src("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"));
        this.head.addInner(new script().src("https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"));
        this.head.addInner(new script().src("/resources/html/js/ie.js"));
    	this.body=html.returnAddInner(new body());
	}

    public BootStrapPage(String title,String lang)
    {
        this(title,lang,null);
    }  
    public BootStrapPage(String title)
    {
        this(title,"en");
    }
    public BootStrapPage()
    {
        this(null);
    }
    public Head head()
    {
    	return this.head;
    }
    public body body()
    {
    	return this.body;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.content);
        
    }
    
}
