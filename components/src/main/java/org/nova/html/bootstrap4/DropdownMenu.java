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
package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.div;

public class DropdownMenu extends StyleComponent<DropdownMenu>
{
    public DropdownMenu(StyleComponent<?> button,boolean split)
    {
        super("div", "dropdown-menu");
        button.addClass("dropdown-toggle");
        if (split)
        {
            button.addClass("dropdown-toggle-split");
        }
        button.data("toggle","dropdown");
        
    }
    public DropdownMenu(StyleComponent<?> button)
    {
        this(button,false);
    }
    
    public DropdownMenu right()
    {
        addClass("dropdown-menu-right");
        return this;
    }
    public DropdownMenu addItem(String label,String URL)
    {
        returnAddInner(new a()).addClass("dropdown-item").href(URL).addInner(label);
        return this;
    }
    public DropdownMenu addDivider()
    {
        returnAddInner(new div()).addClass("dropdown-divider");
        return this;
    }
}
