package org.nova.html.bootstrap4;

import org.nova.annotations.Description;
import org.nova.html.bootstrap4.classes.Boundary;
import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.Trigger;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.elements.StringComposer;
import org.nova.html.elements.TagElement;
import org.nova.html.ext.HtmlUtils;
import org.nova.html.tags.script;
import org.nova.json.ObjectMapper;
import org.nova.utils.Utils;

public class Popover
{
    final private TagElement<?> parent;
    
    static class Delay
    {
        public Integer show;
        public Integer hide;
    }
    
    public enum PopoverOption
    {
    	show,
    	hide,
    	toggle,
    	dispose,
    	enable,
    	disable,
    	toggleEnabled,
    	update,
    }
    
    public Popover(TagElement<?> parent)
    {
    	this.parent=parent;
        this.parent.attr("data-toggle","popover");
    }
    
    public Popover title(String title)
    {
        this.parent.attr("title",title);
        return this;
    }
    
    public Popover template(String template)
    {
        this.parent.attr("data-template",template,QuotationMark.SINGLE);
    	return this;
    }
    public Popover animation()
    {
    	return animation(true);
    }

    public Popover animation(boolean animation)
    {
    	if (animation)
    	{
    		this.parent.attr("data-animation",animation);
    	}
        return this;
    }

    public Popover content(String content)
    {
        this.parent.attr("data-content",content,QuotationMark.SINGLE);
        return this;
    }

    public Popover content(Element element,boolean html) throws Throwable
    {
    	/*
        StringComposer composer=new StringComposer();
        element.compose(composer);
        this.content=composer.getStringBuilder().toString();
        this.html=html;
        */
        String content=element.toString();

        this.parent.attr("data-content",content,QuotationMark.SINGLE);
        if (html)
        {
        	this.parent.attr("data-html",true);
        }
        return this;
    }
    public Popover content(Element element) throws Throwable
    {
        return content(element,true);
    }

    @Description("Order is important. Use (hover,focus) and not (focus,hover) to allow buttons on the popover to be clicked.")
    public Popover trigger(Trigger...triggers) throws Exception
    {
        if (triggers.length>1)
        {
            for (Trigger trigger:triggers)
            {
                if (trigger==Trigger.manual)
                {
                    throw new Exception("manual cannot be combined.");
                }
            }
        }
        this.parent.attr("data-trigger",Utils.combine(triggers, " "));
        return this;
    }
    
    public Popover placement(Placement placement)
    {
        this.parent.attr("data-placement",placement);
        return this;
    }
    
    public Popover delay(Integer show,Integer hide)
    {
        Delay delay=new Delay();
        delay.show=show;
        delay.hide=hide;
        try
        {
            this.parent.attr("data-delay",delay,QuotationMark.SINGLE);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public Popover container(String container)
    {
        this.parent.attr("data-container",container);
        return this;
    }
    
    public Popover offset(int offsetX,int offsetY)
    {
        this.parent.attr("data-offset",offsetX+" "+offsetY);
    	return this;
    }
    public Popover boundar(Boundary boundary)
    {
    	this.parent.attr("data-boundary",boundary);
    	return this;
    }
    
    public String js_popover(PopoverOption option,QuotationMark mark)
    {
//    	return "document.getElementById("+mark+this.parent.id()+mark+").popover("+mark+option+mark+")";
    	return "$("+mark+"#"+this.parent.id()+mark+").popover("+mark+option+mark+")";
    }
    public String js_popover(PopoverOption option)
    {
    	return js_popover(option, QuotationMark.APOS);
    }
    
    public static script js_ready()
    {
        return new script().addInner("$(document).ready(function(){$('[data-toggle=\"popover\"]').popover();});");
    }

    
    
}
