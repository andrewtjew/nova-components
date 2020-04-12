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
package org.nova.html.ext;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.tags.div;
import org.nova.html.tags.script;

public class Refresher extends Element
{
    final private script script;
    final private GlobalTagElement<?> element;
    public Refresher(GlobalTagElement<?> container,String url,long interval,String timerName) throws Exception
    {
        String id=container.id();
        if (id==null)
        {
            id="_"+container.hashCode();
            container.id(id);
        }
        StringBuilder sb=new StringBuilder();
        if (timerName!=null)
        {
            sb.append("var "+timerName+"=");
        }
        sb.append("window.setInterval(function(){$.get('"+url+"',function(data){$('#"+id+"').html(data);});},"+interval+");");
        this.script=new script().addInner(sb.toString());
        this.element=container;
    }

    public Refresher(String id,String url,long interval,String timerName) throws Exception
    {
        this(new div().id(id),url,interval,timerName);
    }
    public Refresher(String url,long interval,String timerName) throws Exception
    {
        this(new div(),url,interval,timerName);
    }
    public Refresher(String url,long interval) throws Exception
    {
        this(url,interval,null);
    }

    public Refresher setInitial(Element element)
    {
        this.element.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.getStringBuilder().append(script.toString());
        composer.compose(this.element);
        
    }

}
