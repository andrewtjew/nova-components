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

public class Popover extends TipComponent<Popover> 
{
    public Popover(TagElement<?> toggler)
    {
    	super(toggler,"popover");
    }
    
    public String js_popover(TipOption option,QuotationMark mark)
    {
//    	return "document.getElementById("+mark+this.parent.id()+mark+").popover("+mark+option+mark+")";
    	return "$("+mark+"#"+this.toggler.id()+mark+").popover("+mark+option+mark+")";
    }
    public String js_popover(TipOption option)
    {
    	return js_popover(option, QuotationMark.APOS);
    }
    
    public static script js_ready()
    {
        return new script().addInner("$(document).ready(function(){$('[data-toggle=\"popover\"]').popover();});");
    }

    
    
}
