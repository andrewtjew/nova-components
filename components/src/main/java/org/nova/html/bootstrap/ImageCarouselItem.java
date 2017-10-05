package org.nova.html.bootstrap;

import org.nova.html.tags.img;

public class ImageCarouselItem extends CarouselItem 
{
	public ImageCarouselItem(String src,String alt,boolean active)
	{
		super(active);
		this.addInner(new img().style("width:100%;").src(src).alt(alt));
	}
	public ImageCarouselItem(String src,String alt)
	{
		this(src,alt,false);
	}
	public ImageCarouselItem(String src,boolean active)
	{
		super(active);
		this.addInner(new img().style("width:100%;").src(src));
	}
	public ImageCarouselItem(String src)
	{
		this(src,false);
	}
}
