package org.nova.html.bootstrap;

import org.nova.html.tags.*;

public class Nav extends ul
{
	public Nav(nav class_)
	{
		this.class_(class_.toString());
	}
	public Nav add(a a,String class_)
	{
		li li=this.returnAddInner(new li()).attr("role","presentation");
		if (class_!=null)
		{
			li.class_(class_);
		}
		li.addInner(a);
		return this;
	}
	public Nav add(a a)
	{
		return add(a,null);
	}
	public Nav add(String href,String text,String class_)
	{
		return add(new a().href(href).addInner(text),class_);
	}
	public Nav add(String href,String text)
	{
		return add(href,text,null);
	}
}
