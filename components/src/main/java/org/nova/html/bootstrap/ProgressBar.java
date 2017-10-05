package org.nova.html.bootstrap;

import org.nova.html.bootstrap.attributes.classes.progress_bar;
import org.nova.html.tags.div;

public class ProgressBar extends div 
{
	public ProgressBar(progress_bar class_,int width,String label,int aria_valuenow,int aria_valuemin,int aria_valuemax)
	{
		this.class_(class_.toString());
		this.style("width:"+width+"%;");
		this.attr("role", "progressbar");
		this.attr("aria-valuenow",aria_valuenow);
		this.attr("aria-valuemin",aria_valuemin);
		this.attr("aria-valuemax",aria_valuemax);
		if (label!=null)
		{
			this.addInner(label);
		}
	}
	public ProgressBar(progress_bar class_,int width,String label)
	{
		this(class_,width,label,width,0,100);
	}
	public ProgressBar(progress_bar class_,int width)
	{
		this(class_,width,null);
	}
	public ProgressBar(int width)
	{
		this(new progress_bar(),width,width+"%");
	}
}
