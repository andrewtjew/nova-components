package org.nova.html.bootstrap;

import org.nova.html.bootstrap.attributes.classes.btn;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.ul;
import org.nova.html.widgets.MenuBar;
import org.nova.html.tags.span;

public class DropdownMenu extends ul
{
	public DropdownMenu(String href,String text)
	{
		class_("dropdown-menu");
	}
	public DropdownMenu add(String href,String text)
	{
		return add(new a().href(href).addInner(text));
	}
	public DropdownMenu add(a a)
	{
		this.addInner(new li().addInner(a));
		return this;
	}
	public DropdownMenu addSeperator()
	{
		this.addInner(new li().attr("role","seperator").class_("divider"));
		return this;
	}
}
