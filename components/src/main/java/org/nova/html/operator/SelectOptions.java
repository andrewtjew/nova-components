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
package org.nova.html.operator;

import org.nova.html.tags.option;
import org.nova.html.tags.select;

public class SelectOptions extends select
{
    public SelectOptions()
    {
    }
    public SelectOptions(String name)
    {
        name(name);
    }
    public SelectOptions add(Object value,String text,boolean selected)
    {
        addInner(new option().value(value).addInner(text).selected(selected));
        return this;
    }
    public SelectOptions add(String value,String text,String currentValue)
    {
        addInner(new option().value(value).addInner(text).selected(value.equals(currentValue)));
        return this;
    }
    public SelectOptions add(Object value,boolean selected)
    {
        String text=value.toString();
        return add(text,text,selected);
    }
    public SelectOptions add(Object value,String text)
    {
        addInner(new option().value(value).addInner(text));
        return this;
    }
    public SelectOptions add(String value)
    {
        return add(value,value);
    }
    public SelectOptions addOption(Object value)
    {
        String text=value.toString();
        return add(text,text);
    }
}
