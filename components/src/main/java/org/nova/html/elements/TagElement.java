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
package org.nova.html.elements;

import org.nova.html.ext.HtmlUtils;

public class TagElement<ELEMENT extends TagElement<ELEMENT>> extends InnerElement<ELEMENT>
{
    private String id;
    final private StringBuilder sb;
    final private String tag;
    final private boolean noEndTag;
    final private StringBuilder classBuilder;
    
    public TagElement(String tag,boolean noEndTag)
    {
        this.tag=tag;
        this.sb=new StringBuilder();
        this.noEndTag=noEndTag;
        this.classBuilder=new StringBuilder();
    }
    public TagElement(String tag)
    {
        this(tag,false);
    }
    public ELEMENT addClass(String class_)
    {
        if (class_!=null)
        {
            if (this.classBuilder.length()>0)
            {
                this.classBuilder.append(' ');
            }
            this.classBuilder.append(class_);
        }
        return (ELEMENT) this;
    }
    public ELEMENT id(String value)
    {
        if (value!=null)
        {
            this.id=value;
        }
        else
        {
//            id();
        }
        return (ELEMENT) this;
    }
    /*
    public ELEMENT autoid()
    {
        this.id="_"+this.hashCode();
        return (ELEMENT) this;
    }
    */
    public String id()
    {
        if (this.id==null)
        {
            this.id="_"+this.hashCode();
            return this.id;
        }
        return this.id;
    }
    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name,Object value)
    {
        return attr(name,value,QuotationMark.DOUBLE);
    }
    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name,Object value,QuotationMark quotationMark)
    {
        if (value!=null)
        {
            this.sb.append(' ').append(name).append("=").append(quotationMark.toString()).append(value).append(quotationMark.toString());
        }
        return (ELEMENT) this;
    }

    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name)
    {
        sb.append(' ').append(name);
        return (ELEMENT) this;
    }

    public String class_()
    {
        return this.classBuilder.toString();
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (this.classBuilder.length()>0)
        {
            attr("class",this.classBuilder.toString());
        }
        attr("id",this.id);
        
        StringBuilder composerStringBuilder=composer.getStringBuilder();
        composerStringBuilder.append('<').append(this.tag);
        composerStringBuilder.append(this.sb.toString());
        composerStringBuilder.append('>');
        if (this.noEndTag==false)
        {
            super.compose(composer);
            composerStringBuilder=composer.getStringBuilder();
            composerStringBuilder.append("</").append(this.tag).append('>');
        }
    }
}
