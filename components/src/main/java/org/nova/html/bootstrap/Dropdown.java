package org.nova.html.bootstrap;

import org.nova.html.bootstrap.attributes.classes.btn;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.ul;
import org.nova.html.widgets.MenuBar;
import org.nova.html.tags.span;

public class Dropdown extends div
{
	final private ul ul;
	
	public Dropdown(String text)
	{
		this.class_("dropdown");
		this.addInner(
				new a()
				.class_("dropdown-toggle")
				.attr("data-toggle","dropdown")
				.attr("role","button")
				.addInner(text)
				.addInner(new span().class_("caret"))
				);
		this.ul=this.returnAddInner(new ul()).class_("dropdown-menu");
	}
	public Dropdown add(String href,String text)
	{
		return add(new a().href(href).addInner(text));
	}
	public Dropdown add(a a)
	{
		this.ul.addInner(new li().addInner(a));
		return this;
		
	}
	public Dropdown addSeperator()
	{
		this.ul.addInner(new li().attr("role","seperator").class_("divider"));
		return this;
	}
	
}
