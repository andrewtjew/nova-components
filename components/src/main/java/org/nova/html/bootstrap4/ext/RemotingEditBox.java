/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.html.bootstrap4.ext;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.display;
import org.nova.html.attributes.unit;
import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ClassBuilder;
import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.Popover;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.TipOption;
import org.nova.html.bootstrap4.classes.Display;
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
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.HtmlUtils;
import org.nova.html.tags.div;
import org.nova.html.tags.script;
import org.nova.html.ext.ModalBackground;
import org.nova.html.remoting1.CallBuilder;
import org.nova.http.client.PathAndQuery;

public class RemotingEditBox extends StyleComponent<RemotingEditBox>   
{
	public RemotingEditBox(ModalBackground background,StyleComponent<?> valueElement,String action,InputElement<?> inputElement,GlobalEventTagElement<?> editButton,GlobalEventTagElement<?> acceptButton,GlobalEventTagElement<?> dismissButton,Placement placement) throws Throwable
	{
        super("div",null);
		this.d(Display.flex);
		valueElement.flex(Flex.grow,1);
		this.addInner(valueElement);
		QuotationMark mark=QuotationMark.APOS;

		String template;
        template="<div class='popover' role='tooltip' style='margin:0;padding:0;width:inherit;max-width:100% !important;'><div class='popover-body'></div></div>";

		
		
		editButton.style(new Style().display(display.none));

		this.addInner(editButton);
		this.onmouseover("document.getElementById("+mark+editButton.id()+mark+").style.display="+mark+"block"+mark);
		this.onmouseleave("document.getElementById("+mark+editButton.id()+mark+").style.display="+mark+"none"+mark);

		Item inputGroup=new Item().d(Display.flex).mx(0).px(0);
		inputElement.addClass(new ClassBuilder().w(100).toString());
		inputGroup.addInner(inputElement);
		inputGroup.addInner(acceptButton);
		inputGroup.addInner(dismissButton);

		editButton.onclick(HtmlUtils.js_call("Remoting.openEditBox"
                , template
                ,background!=null?background.id():null
                ,id()
                ,acceptButton.id()
                ,dismissButton.id()
                ,editButton.id()
                ,inputElement.id()
                ,valueElement.id()
                ,action
                ,inputGroup.getHtml()
                ,placement
                ));


	}
	public RemotingEditBox(ModalBackground background,StyleComponent<?> valueElement,String action,InputElement<?> inputElement,Placement placement) throws Throwable
	{
		this(background,valueElement,action,inputElement
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x270F;")
				,new Button().ml(1).color(StyleColor.success).addInner("&#x2713;")
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
				,placement
				);
	}
    public RemotingEditBox(ModalBackground background,Object value,String action,InputElement<?> inputElement,Placement placement) throws Throwable
    {
        this(background,new Item().m(2).text(TextStyle.truncate).addInner(value),action,inputElement,placement);
    }

    public RemotingEditBox(ModalBackground background,Object value,String action,InputElement<?> inputElement) throws Throwable
    {
        this(background,new Item().m(2).text(TextStyle.truncate).addInner(value),action,inputElement,Placement.bottom);
    }
}
