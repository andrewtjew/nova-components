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
package org.nova.html.tags;

import org.nova.html.elements.ButtonElement;
import org.nova.html.enums.target;

public class button_submit extends ButtonElement<button_submit>
{
    public button_submit()
    {
        super();
        attr("type","submit");
    }
    public button_submit formaction(String URL) 
    {
        return attr("formaction",URL);
    }
    public button_submit formenctype(String value)
    {
        return attr("formenctype",value);
    }
    public button_submit formmethod(String value) 
    {
        return attr("formmethod",value);
    }
    public button_submit formnovalidate()  //submit
    {
        return attr("formnovalidate");
    }
    public button_submit formnovalidate(boolean formnovalidate)
    {
        if (formnovalidate)
        {
            attr("formnovalidate");
        }
        return this;
    }
    public button_submit formtarget(target target)
    {
        return attr("target",target);
    }
    public button_submit formtarget(String framename)
    {
        return attr("target",framename);
    }
}
