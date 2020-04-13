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
import org.nova.html.bootstrap4.classes.Align;
import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.Boundary;
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
import org.nova.html.elements.TagElement;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.Head;
import org.nova.html.ext.HtmlUtils;
import org.nova.html.tags.script;
import org.nova.html.ext.ModalBackground;
import org.nova.html.remoting1.CallBuilder;
import org.nova.http.client.PathAndQuery;

public class RemotingLabelPopover 
{
	public RemotingLabelPopover(QuotationMark mark,GlobalEventTagElement<?> toggler,ModalBackground background,String action,String label,GlobalEventTagElement<?> acceptButton,GlobalEventTagElement<?> dismissButton,Placement placement) throws Throwable
	{

//		template="<div class='popover' role='tooltip' style='margin:0;padding:0;'><div class='popover-body'></div></div>";

		String template=new Item().addClass("popover").attr("role","tooltip").style(new Style().margin(new Size(0,unit.em)).padding(new Size(0,unit.em)))
		        .addInner(new Item().addClass("popover-body")).getHtml(QuotationMark.DOUBLE);
		
        Item inputGroup=new Item().d(Display.flex).mx(0).px(0);
		inputGroup.addInner(new Item().mr(2).align_self(AlignSelf.center).addInner(label));
		inputGroup.addInner(acceptButton);
		inputGroup.addInner(dismissButton);

        toggler.onclick(HtmlUtils.js_call(mark,"Remoting.openLabel"
                , template
                ,background!=null?background.id():null
                ,toggler.id()
                ,acceptButton.id()
                ,dismissButton.id()
                ,action
                ,inputGroup.getHtml()
                ,placement
                ));
		
	}
	public RemotingLabelPopover(QuotationMark mark,GlobalEventTagElement<?> toggler,ModalBackground background,String action,String label,Placement placement) throws Throwable
	{
		this(mark,toggler,background,action,label
				,new Button().ml(1).color(StyleColor.success).addInner("&#x2713;")
				,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
				,placement
				);
	}
    public RemotingLabelPopover(QuotationMark mark,GlobalEventTagElement<?> toggler,String action,String label,Placement placement) throws Throwable
    {
        this(mark,toggler,null,action,label
                ,new Button().ml(1).color(StyleColor.success).addInner("&#x2713;")
                ,new Button().ml(1).color(StyleColor.secondary).addInner("&#x1f5d9;")
                ,placement
                );
    }
	public RemotingLabelPopover(GlobalEventTagElement<?> toggler,ModalBackground background,String action,String label) throws Throwable
	{
		this(QuotationMark.SINGLE,toggler,background,action,label,Placement.right);
	}

}
