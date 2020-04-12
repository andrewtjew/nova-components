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
package org.nova.html.deprecated;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;

public class SideNavigationMenu extends div
{
    final private String id;
    final private int width;
    public SideNavigationMenu(Head head,String id,int width,String sourcePath,String cssFile) throws Exception
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        if (head!=null)
        {
            head.add(SideNavigationMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
        this.id=id;
        this.width=width;
        id(id);
        addClass("sidenav");
        this.addInner(new a().href("javascript:void(0)").addClass("closebtn").onclick("document.getElementById('"+id+"').style.width = '0';").addInner("&times;"));
    }
    public SideNavigationMenu(Head head,String id,int width) throws Exception
    {
        this(head,id,width,"/resources/html","/w3c/SideNavigationMenu/style.css");
    }
    public SideNavigationMenu(Head head,int width) throws Exception
    {
        this(head,null,width);
    }
    public SideNavigationMenu(int width) throws Exception
    {
        this(null,width);
    }

    public SideNavigationMenu addMenuItem(a a)
    {
        this.addInner(a);
        return this;
    }
    public SideNavigationMenu addMenuItem(String name,String href)
    {
        return addMenuItem(new a().href(href).addInner(name));
    }
    public SideNavigationMenu openBy(GlobalEventTagElement<? extends TagElement<?>> element)
    {
        element.onclick("document.getElementById('"+this.id+"').style.width = '"+this.width+"px';");
        return this;
    }
}
