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

public class Tooltip extends TipComponent<Tooltip>
{
    public Tooltip(TagElement<?> toggler)
    {
    	super(toggler,"tooltip");
    }
    
    public String js_tooltip(TipOption option,QuotationMark mark)
    {
    	return "$("+mark+"#"+this.toggler.id()+mark+").tooltip("+mark+option+mark+")";
    }
    public String js_tooltip(TipOption option)
    {
    	return js_tooltip(option, QuotationMark.APOS);
    }
    
    public static script js_ready()
    {
        return new script().addInner("$(document).ready(function(){$('[data-toggle=\"tooltip\"]').tooltip();});");
    }
}
