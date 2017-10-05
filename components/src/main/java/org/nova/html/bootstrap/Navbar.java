package org.nova.html.bootstrap;

import org.nova.html.bootstrap.attributes.classes.navbar;
import org.nova.html.elements.Element;
import org.nova.html.tags.*;

public class Navbar extends div
{
	final private div container;
	final private ul ul;
	public Navbar(navbar class_)
	{
		this.class_(class_.toString());
		this.container=this.returnAddInner(new div()).class_("container-fluid");
		this.ul=this.container.returnAddInner(new ul()).class_("nav navbar-nav");
	}
	public Navbar()
	{
		this(new navbar().default_());
	}
	
	public Navbar add(Element element)
	{
		this.ul.addInner(element);
		return this;
	}
}
