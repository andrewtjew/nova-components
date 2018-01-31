package org.nova.html.bootstrap;

import org.nova.html.tags.div;

public class CarouselItem extends div 
{
	final boolean active;
	public CarouselItem(boolean active)
	{
		this.active=active;
		if (active)
		{
			this.class_("item active");
		}
		else
		{
			this.class_("item");
		}
	}
	public CarouselItem()
	{
		this(false);
	}
}
