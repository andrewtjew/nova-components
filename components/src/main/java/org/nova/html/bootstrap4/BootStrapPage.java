package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.character_set;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.link_rel;
import org.nova.html.enums.name;
import org.nova.html.tags.body;
import org.nova.html.tags.html;
import org.nova.html.tags.link;
import org.nova.html.tags.meta;
import org.nova.html.tags.script;
import org.nova.html.tags.head;
import org.nova.html.widgets.BasicPage;
import org.nova.html.widgets.Content;
import org.nova.html.widgets.DocType;
import org.nova.html.widgets.Head;

public class BootStrapPage extends Element
{
    final private Head head;
    final private body body;
    final private Content content;
    public BootStrapPage(String title,String lang)
	{
        this.content=new Content();
    	html html=this.content.returnAddInner(new html().lang(lang));
    	
    	this.head=html.returnAddInner(new Head());
//    	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    	//<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>    	
    	this.head.title(title);
    	this.head.add(new meta().name(name.viewport).content("width=device-width, initial-scale=1, shrink-to-fit=no"));
    	this.head.add(new meta().charset(character_set.UTF_8));
//    	this.head.add(new script().src("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js").integrity("sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl").crossorigin(crossorigin.anonymous));
//        this.head.add(new link().rel(link_rel.stylesheet).href("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css").integrity("sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm").crossorigin(crossorigin.anonymous));
//        this.head.add(new script().src("https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js").integrity("sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb").crossorigin(crossorigin.anonymous));
    	this.head.add(new link().rel(link_rel.stylesheet).href("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"));
        this.head.add(new script().src("https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"));
        this.head.add(new script().src("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"));
        this.head.add(new script().src("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"));
    	this.body=html.returnAddInner(new body());
	}

  
    public BootStrapPage(String title)
	{
    	this(title,"en");
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
        composer.render(this.content);
        
    }
    
}
