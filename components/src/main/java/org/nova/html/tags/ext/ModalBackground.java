package org.nova.html.tags.ext;
import org.nova.html.tags.html;
import org.nova.html.attributes.Color;
import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.display;
import org.nova.html.attributes.overflow;
import org.nova.html.attributes.position;
import org.nova.html.attributes.unit;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.ext.Content;
import org.nova.html.ext.DocType;
import org.nova.html.ext.Head;
import org.nova.html.tags.body;
import org.nova.html.tags.div;

public class ModalBackground extends GlobalEventTagElement<div>
{
	final private int z_index;
    public ModalBackground(int z_index,Color color,boolean show)
    {
        super("div");
        Style style=new Style()
        		.position(position.fixed)
        		.z_index(z_index)
        		.left(new Size(0,unit.px))
           		.top(new Size(0,unit.px))
        		.width(new Size(100,unit.percent))
           		.height(new Size(100,unit.percent))
           		.overflow(overflow.auto)
           		.background_color(color);
        if (show)
        {
        	style.display(display.block);
        }
        else
        {
        	style.display(display.none);
        }
        this.style(style);
        this.z_index=z_index;
    }
    public ModalBackground()
    {
    	this(1,Color.rgba(0, 0, 0, 0.0f),false);
    }
    public String js_show(QuotationMark mark)
    {
    	return "document.getElementById("+mark+id()+mark+").style.display="+mark+"block"+mark;
    }
    public int z_index()
    {
    	return this.z_index;
    }
    public String js_show()
    {
    	return js_show(QuotationMark.APOS);
    }
    public String js_hide(QuotationMark mark)
    {
    	return "document.getElementById("+mark+id()+mark+").style.display="+mark+"none"+mark;
    }
    public String js_hide()
    {
    	return js_hide(QuotationMark.APOS);
    }
}
