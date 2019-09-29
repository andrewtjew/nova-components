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
import org.nova.html.bootstrap4.Popover.PopoverOption;
import org.nova.html.bootstrap4.classes.Flex;
import org.nova.html.bootstrap4.classes.Justify;
import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.bootstrap4.classes.Trigger;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.elements.InputElement;
import org.nova.html.elements.QuotationMark;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.remoting.CallBuilder;
import org.nova.html.tags.ext.ModalBackground;
import org.nova.http.client.PathAndQuery;

public class RemotingInput extends StyleComponent<RemotingInput>   
{
	public RemotingInput(ModalBackground background,StyleComponent<?> valueElement,PathAndQuery pathAndQuery,InputElement<?> inputElement,GlobalEventTagElement<?> editButton,GlobalEventTagElement<?> acceptButton,GlobalEventTagElement<?> dismissButton) throws Throwable
	{
        super("div",null);
		style(new Style().z_index(background.z_index()+1));
        Item container=this.returnAddInner(new Item());
		container.d_flex();//.justify_content(Justify.center);
		valueElement.flex(Flex.grow,1);
		container.addInner(valueElement);
//		inputElement.style("width:1000px;");
		QuotationMark mark=QuotationMark.APOS;

		Popover pop=new Popover(this);
		editButton.style(new Style().display(display.none));
		editButton.onclick("document.getElementById("+mark+editButton.id()+mark+").style.display="+mark+"none"+mark+";"+pop.js_popover(PopoverOption.show)+";"+"document.getElementById("+mark+inputElement.id()+mark+").focus();"+background.js_show()+";");
		container.addInner(editButton);
		this.onmouseover("document.getElementById("+mark+editButton.id()+mark+").style.display="+mark+"block"+mark);
		this.onmouseleave("document.getElementById("+mark+editButton.id()+mark+").style.display="+mark+"none"+mark);

		Item inputGroup=new Item().d_flex();
		inputGroup.addInner(inputElement);

		String js_close=pop.js_popover(PopoverOption.hide)+";"+background.js_hide()+";";
		FormQueryBuilder query=new FormQueryBuilder(mark);
		query.value(inputElement);
		query.add("_valueElementId",valueElement.id());
		pathAndQuery.addQuery("_valueElementId",valueElement.id());
		String js_submit=new CallBuilder(mark).generatePost(pathAndQuery,query)+";"+js_close;
		acceptButton.onclick(js_submit);
		inputGroup.addInner(acceptButton);

		dismissButton.onclick(js_close);
		inputGroup.addInner(dismissButton);

		pop.content(inputGroup);
		pop.trigger(Trigger.manual);
		pop.placement(Placement.bottom);
	}
	public RemotingInput(ModalBackground background,StyleComponent<?> valueElement,PathAndQuery pathAndQuery,InputElement<?> inputElement) throws Throwable
	{
		this(background,valueElement,pathAndQuery,inputElement
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x270F;")
				,new Button().ml(1).color(StyleColor.primary).addInner("&#x2713;")
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
				);
	}
	public RemotingInput(ModalBackground background,Object value,PathAndQuery pathAndQuery,InputElement<?> inputElement) throws Throwable
	{
		this(background,new Item().m(2).addInner(value),pathAndQuery,inputElement);
	}
}
