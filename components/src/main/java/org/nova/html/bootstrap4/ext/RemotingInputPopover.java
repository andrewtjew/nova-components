package org.nova.html.bootstrap4.ext;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.display;
import org.nova.html.attributes.unit;
import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.FormControlInputText;
import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.Popover;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.TipOption;
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

public class RemotingInputPopover extends Popover    
{
	public RemotingInputPopover(TagElement<?> toggler,ModalBackground background,Size width,PathAndQuery pathAndQuery,InputElement<?> inputElement,GlobalEventTagElement<?> acceptButton,GlobalEventTagElement<?> dismissButton,Placement placement) throws Throwable
	{
		super(toggler);
		RemotingInputPopover container=this;//.returnAddInner(new Item());
		QuotationMark mark=QuotationMark.APOS;

		String template;

		String widthText=width.toString();
		template="<div class='popover' role='tooltip' style='margin:0;padding:0;max-width:100% !important;width:"+widthText+";'><div class='popover-body'></div></div>";

		template=HtmlUtils.escapeQuotes(template);
		this.template(template);
//		pop.container("#"+valueElement.id());
//		this.container("#"+this.id());
		
		Item inputGroup=new Item().d_flex();
		inputGroup.addInner(inputElement);

		String js_close=js_popover(TipOption.hide)+";"+background.js_hide()+";";
		FormQueryBuilder query=new FormQueryBuilder(mark);
		query.value(inputElement);
		String js_submit=new CallBuilder(mark).js_post(pathAndQuery,query)+";"+js_close;
		acceptButton.onclick(js_submit);
		inputGroup.addInner(acceptButton);
		

		dismissButton.onclick(js_close);
		inputGroup.addInner(dismissButton);

		content(inputGroup);
		//trigger(Trigger.manual);
		placement(placement);

//        String script="$('#"+toggler.id()+"').on('show.bs.popover',function(){"+background.js_show()+";});";
//        +"document.getElementById("+mark+inputElement.id()+mark+").focus();"

        String showScript="$('#"+toggler.id()+"').on('show.bs.popover',function(){"+background.js_show()+";});";
        toggler.addInner(new script().addInner(showScript));
        String shownScript="$('#"+toggler.id()+"').on('shown.bs.popover',function(){document.getElementById('"+inputElement.id()+"').focus();});";//
        toggler.addInner(new script().addInner(shownScript));
		
	}
	public RemotingInputPopover(TagElement<?> toggler,ModalBackground background,Size width,PathAndQuery pathAndQuery,InputElement<?> inputElement,Placement placement) throws Throwable
	{
		this(toggler,background,width,pathAndQuery,inputElement
				,new Button().ml(1).color(StyleColor.success).addInner("&#x2713;")
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
				,placement
				);
	}
	public RemotingInputPopover(TagElement<?> toggler,ModalBackground background,Size width,PathAndQuery pathAndQuery,InputElement<?> inputElement) throws Throwable
	{
		this(toggler,background,width,pathAndQuery,inputElement,Placement.right);
	}
}
