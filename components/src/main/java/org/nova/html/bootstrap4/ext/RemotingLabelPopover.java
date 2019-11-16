package org.nova.html.bootstrap4.ext;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.display;
import org.nova.html.attributes.unit;
import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.Popover;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.TipOption;
import org.nova.html.bootstrap4.classes.Align;
import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.Flex;
import org.nova.html.bootstrap4.classes.Justify;
import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.bootstrap4.classes.TextStyle;
import org.nova.html.bootstrap4.classes.Trigger;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.elements.InputElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.elements.TagElement;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.Head;
import org.nova.html.ext.HtmlUtils;
import org.nova.html.remoting.CallBuilder;
import org.nova.html.tags.script;
import org.nova.html.ext.ModalBackground;
import org.nova.http.client.PathAndQuery;

public class RemotingLabelPopover extends Popover    
{
	public RemotingLabelPopover(TagElement<?> toggler,ModalBackground background,String js_onAccept,String label,GlobalEventTagElement<?> acceptButton,GlobalEventTagElement<?> dismissButton,Placement placement) throws Throwable
	{
		super(toggler);
		RemotingLabelPopover container=this;//.returnAddInner(new Item());
		QuotationMark mark=QuotationMark.APOS;

		String template;

		template="<div class='popover' role='tooltip' style='margin:0;padding:0;'><div class='popover-body'></div></div>";

		template=HtmlUtils.escapeQuotes(template);
		this.template(template);
//		pop.container("#"+valueElement.id());
//		this.container("#"+this.id());
		
		Item inputGroup=new Item().d_flex();
		inputGroup.addInner(new Item().mr(2).align_self(AlignSelf.center).addInner(label));

		String js_close=js_popover(TipOption.hide)+";"+background.js_hide()+";";
		FormQueryBuilder query=new FormQueryBuilder(mark);
		String js_submit=js_onAccept+";"+js_close;
		acceptButton.onclick(js_submit);
		inputGroup.addInner(acceptButton);
		

		dismissButton.onclick(js_close);
		inputGroup.addInner(dismissButton);

		content(inputGroup);
		//trigger(Trigger.manual);
		placement(placement);
		
		String script="$('#"+toggler.id()+"').on('show.bs.popover',function(){"+background.js_show()+";});";
		toggler.addInner(new script().addInner(script));
		
	}
	public RemotingLabelPopover(TagElement<?> toggler,ModalBackground background,String js_onAccept,String label,Placement placement) throws Throwable
	{
		this(toggler,background,js_onAccept,label
				,new Button().ml(1).color(StyleColor.success).addInner("&#x2713;")
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
				,placement
				);
	}
	public RemotingLabelPopover(TagElement<?> toggler,ModalBackground background,String js_onAccept,String label) throws Throwable
	{
		this(toggler,background,js_onAccept,label,Placement.right);
	}
}
