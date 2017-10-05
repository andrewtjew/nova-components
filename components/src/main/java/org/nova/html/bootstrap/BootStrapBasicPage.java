package org.nova.html.bootstrap;

import org.nova.html.enums.character_set;
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

public class BootStrapBasicPage extends Content
{
    final private head head;
    final private body body;
    
    public BootStrapBasicPage(String title,String lang,String bootstrapCssSource,String jquerySource,String bootstrapStyleSource)
	{
    	addInner(new html().lang("en"));
    	this.head=returnAddInner
    	(
    		new head()
    		.title(title)
    		.addInner(new meta().charset(character_set.UTF_8))
    		.addInner(new meta().name(name.viewport).content("width=device-width,initial-scale=1"))
    		.addInner(new script().src(jquerySource))
    		.addInner(new script().src(bootstrapStyleSource))
    		.addInner(new link().rel(link_rel.stylesheet).href(bootstrapCssSource))
    	);
    	this.body=returnAddInner(new body());
	}
    public BootStrapBasicPage(String title,String lang,String bootstrapStyleSource)
	{
    	this(title,lang,bootstrapStyleSource,"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js","https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js");
	}
    public BootStrapBasicPage(String title,String lang)
	{
    	this(title,lang,"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css");
	}
    public BootStrapBasicPage(String title)
	{
    	this(title,"en");
	}
    public head head()
    {
    	return this.head;
    }
    public body body()
    {
    	return this.body;
    }
    
}
