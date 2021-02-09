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
package org.nova.html.bootstrap;

import org.nova.html.attributes.Style;
import org.nova.html.bootstrap.classes.Align;
import org.nova.html.bootstrap.classes.AlignItems;
import org.nova.html.bootstrap.classes.AlignSelf;
import org.nova.html.bootstrap.classes.DeviceClass;
import org.nova.html.bootstrap.classes.Display;
import org.nova.html.bootstrap.classes.Edge;
import org.nova.html.bootstrap.classes.Flex;
import org.nova.html.bootstrap.classes.Float_;
import org.nova.html.bootstrap.classes.Font;
import org.nova.html.bootstrap.classes.Justify;
import org.nova.html.bootstrap.classes.Overflow;
import org.nova.html.bootstrap.classes.Position;
import org.nova.html.bootstrap.classes.Rounded;
import org.nova.html.bootstrap.classes.StyleColor;
import org.nova.html.bootstrap.classes.TextAlign;
import org.nova.html.bootstrap.classes.TextStyle;
import org.nova.html.elements.Composer;
import org.nova.html.elements.TagElement;

public interface Styling
{
//    final private TagElement<?> element;
    public Styling addClass(Object class_,Object...fragments);
    public TagElement<?> getElement();


    public default Styling color(StyleColor value,boolean outline)
    {
        TagElement<?> element=getElement();
        if (element instanceof Component<?>)
        {
            Component<?> component=(Component<?>)element;
            if (outline)
            {
                addClass(component.getComponentClass(),outline?"outline":null,value.toString());
            }
            else
            {
                addClass(component.getComponentClass(),value.toString());
            }
        }
        
        return this;
    }

    @SuppressWarnings("unchecked")
    public default Styling color(StyleColor value)
    {
        return color(value,false);
    }

//    public default Styling addClass(Object class_,Object...fragments)
//    {
//        if (fragments!=null)
//        {
//            if (class_!=null)
//            {
//                StringBuilder sb=new StringBuilder(class_.toString());
//                for (Object fragment:fragments)
//                {
//                    if (fragment!=null)
//                    {
//                        sb.append('-').append(fragment);
//                    }
//                }
//                this.element.addClass(sb.toString());
//            }
//        }
//        return this;
//    }

    public default Styling col(DeviceClass deviceClass,int columns)
    {
        if (deviceClass==DeviceClass.xs)
        {
            return addClass("col",columns);
        }
        return addClass("col",deviceClass,columns);
    }
    public default Styling col(DeviceClass deviceClass)
    {
        if (deviceClass==DeviceClass.xs)
        {
            return addClass("col");
        }
        return addClass("col",deviceClass);
    }
    public default Styling col(int columns)
    {
        return addClass("col",columns);
    }
    public default Styling col()
    {
        return addClass("col");
    }
    public default Styling float_(DeviceClass deviceClass,Float_ value)
    {
        return addClass("float",deviceClass,value);
    }
    public default Styling form_control()
    {
        return addClass("form-control");
    }

/*
    protected StyleColor color()
    {
        return this.color;
    }
*/
    public default Styling bg(StyleColor value)
    {
        return addClass("bg",value);
    }
    public default Styling bg_gradient(StyleColor value)
    {
        return addClass("bg","gradient",value);
    }
    public default Styling text(StyleColor value)
    {
        return addClass("text",value);
    }
    public default Styling text(TextAlign value)
    {
        return addClass("text",value);
    }
    public default Styling text(DeviceClass deviceClass,TextAlign value)
    {
        return addClass("text",deviceClass,value);
    }
    public default Styling text(TextStyle value)
    {
        return addClass("text",value);
    }
    public default Styling font(Font value)
    {
        return addClass("font",value);
    }
    public default Styling lead()
    {
        return addClass("lead");
    }
    public default Styling small()
    {
        return addClass("small");
    }
    public default Styling float_(Float_ value)
    {
        return addClass("float",value);
    }
    public default Styling offset(int offset)
    {
        return addClass("offset",offset);
    }
    public default Styling display(int size)
    {
        return addClass("display",size);
    }
    public default Styling rounded()
    {
        return addClass("rounded");
    }
    public default Styling rounded(int value)
    {
        return addClass("rounded",value);
    }
    public default Styling rounded(Rounded value)
    {
        return addClass("rounded",value);
    }
    public default Styling border(Edge value)
    {
        return addClass("border",value);
    }
    public default Styling border(Edge value,int size)
    {
        return addClass("border",value,size);
    }
    public default Styling border()
    {
        return addClass("border");
    }
    public default Styling border(StyleColor color)
    {
        addClass("border");
        return addClass("border",color);
    }
    public default Styling border(String color)
    {
        addClass("border");
        return addClass("border",color);
//        return (ClassStyler)this;
    }
    public default Styling clearfix()
    {
        return addClass("clearfix");
    }
    public default Styling flex(Flex flex)
    {
        return addClass("flex",flex);
    }
    public default Styling flex(Flex flex,int value)
    {
        return addClass("flex",flex,value);
    }

    public default Styling flex(DeviceClass deviceClass,Flex flex)
    {
        return addClass("flex",deviceClass,flex);
    }

    public default Styling align_self(AlignSelf value)
    {
        return addClass("align-self",value);
    }
    
    public default Styling align_self(DeviceClass deviceClass,AlignSelf value)
    {
        return addClass("align-self",deviceClass,value);
    }
    
    public default Styling align_items(AlignItems value)
    {
        return addClass("align-items",value);
    }
    
    public default Styling align(Align value)
    {
        return addClass("align",value);
    }
    
    public default Styling order(int value)
    {
        return addClass("order",value);
    }
    
    public default Styling mr(DeviceClass deviceClass,int value)
    {
        return addClass("mr",deviceClass,value);
    }
    public default Styling mr(int value)
    {
        return addClass("mr",value);
    }
    public default Styling ml(int value)
    {
        return addClass("ml",value);
    }
    public default Styling mt(int value)
    {
        return addClass("mt",value);
    }
    public default Styling mb(int value)
    {
        return addClass("mb",value);
    }
    public default Styling mx(int value)
    {
        return addClass("mx",value);
    }
    public default Styling my(int value)
    {
        return addClass("my",value);
    }
    public default Styling m(int value)
    {
        return addClass("m",value);
    }
    //----
    public default Styling mt(DeviceClass deviceClass,int value)
    {
        return addClass("mt",deviceClass,value);
    }
    public default Styling mb(DeviceClass deviceClass,int value)
    {
        return addClass("mb",deviceClass,value);
    }
    public default Styling mx(DeviceClass deviceClass,int value)
    {
        return addClass("mx",deviceClass,value);
    }
    public default Styling my(DeviceClass deviceClass,int value)
    {
        return addClass("my",deviceClass,value);
    }
    public default Styling m(DeviceClass deviceClass,int value)
    {
        return addClass("m",deviceClass,value);
    }
    
    
    
    public default Styling mr_auto()
    {
        return addClass("mr","auto");
    }
    public default Styling ml_auto()
    {
        return addClass("ml","auto");
    }
    public default Styling mt_auto()
    {
        return addClass("mt","auto");
    }
    public default Styling mb_auto()
    {
        return addClass("mb","auto");
    }
    public default Styling mx_auto()
    {
        return addClass("mx","auto");
    }
    public default Styling my_auto()
    {
        return addClass("my","auto");
    }
    public default Styling me_auto()
    {
        return addClass("me","auto");
    }
    public default Styling ms_auto()
    {
        return addClass("ms","auto");
    }

    public default Styling pr(int value)
    {
        return addClass("pr",value);
    }
    public default Styling pl(int value)
    {
        return addClass("pl",value);
    }
    public default Styling pt(int value)
    {
        return addClass("pt",value);
    }
    public default Styling pb(int value)
    {
        return addClass("pb",value);
    }
    public default Styling px(int value)
    {
        return addClass("px",value);
    }
    public default Styling py(int value)
    {
        return addClass("py",value);
    }
    public default Styling p(int value)
    {
        return addClass("p",value);
    }
    public default Styling pr_auto()
    {
        return addClass("pr","auto");
    }
    public default Styling pl_auto()
    {
        return addClass("pl","auto");
    }
    public default Styling pt_auto()
    {
        return addClass("pt","auto");
    }
    public default Styling pb_auto()
    {
        return addClass("pb","auto");
    }
    public default Styling px_auto()
    {
        return addClass("px","auto");
    }
    public default Styling py_auto()
    {
        return addClass("py","auto");
    }

    public default Styling d(Display display)
    {
        return addClass("d",display);
        
    }
    public default Styling d(DeviceClass deviceClass,Display display)
    {
        return addClass("d",deviceClass,display);
        
    }
    
    public default Styling w(int value)
    {
        return addClass("w",value);
    }
    public default Styling mw(int value)
    {
        return addClass("mw",value);
    }
    
    public default Styling h(int value)
    {
        return addClass("h",value);
    }
    public default Styling h_auto()
    {
        return addClass("h","auto");
    }
    public default Styling mh(int value)
    {
        return addClass("mh",value);
    }
    
    public default Styling position(Position value)
    {
        return addClass("position",value);
        
    }
    public default Styling overflow(Overflow value)
    {
        return addClass("overflow",value);
        
    }
    
    public default Styling justify_content(Justify value)
    {
        return addClass("justify-content",value);
    }
    
    
}
