package org.nova.html.bootstrap;

import org.nova.html.bootstrap.attributes.classes.carousel;
import org.nova.html.bootstrap.attributes.classes.glyphicon;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.ol;
import org.nova.html.tags.span;

public class Carousel extends div 
{
	final private String id;
	final private ol ol;
	final private div wrapper;
	private int index;
	public Carousel(carousel class_,String id,Long dataInterval,String previous,String next,glyphicon previousIcon,glyphicon nextIcon)
	{
		
		this.id(id);
		this.class_(class_.toString());
		this.attr("data-ride","carousel");
		if (dataInterval!=null)
		{
			this.attr("data-interval",dataInterval);
		}
		this.id="#"+id;
		this.ol=this.returnAddInner(new ol()).class_("carousel-indicators");
		this.wrapper=this.returnAddInner(new div()).class_("carousel-inner");
		this.addInner(
				new a()
				.class_("left carousel-control")
				.href(this.id).attr("data-slide","prev")
				.addInner(new span().class_(previousIcon.toString()))
				.addInner(new span().class_("sr-only").addInner(previous))
			);
		this.addInner(
				new a()
				.class_("right carousel-control")
				.href(this.id).attr("data-slide","next")
				.addInner(new span().class_(nextIcon.toString()))
				.addInner(new span().class_("sr-only").addInner(next))
			);
		
	}
	public Carousel(carousel class_,String id,Long dataInterval)
	{
		this(class_,id,dataInterval,"Previous","Next",new glyphicon().chevron_left(),new glyphicon().chevron_right());
	}
	public Carousel(carousel class_,String id)
	{
		this(class_,id, 0L);
	}
	public Carousel(String id)
	{
		this(new carousel().slide(),id);
	}
	
	public Carousel AddCarouselItem(CarouselItem item)
	{
		li li=this.ol.returnAddInner(new li().attr("data-target",this.id).attr("data-slide-to",this.index++));
		if (item.active)
		{
			li.class_("active");
		}
		this.wrapper.addInner(item);
		return this;
	}
}
