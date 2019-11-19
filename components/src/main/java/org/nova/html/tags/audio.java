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

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

public class audio extends GlobalEventTagElement<audio>
{
    public audio()
    {
        super("audio");
    }
    
    public audio autoplay()
    {
        return attr("autoplay");
    }
    public audio autoplay(boolean autoplay)
    {
        if (autoplay)
        {
            return attr("autoplay");
        }
        return this;
    }
    public audio controls()
    {
        return attr("controls");
    }
    public audio controls(boolean controls)
    {
        if (controls)
        {
            return attr("controls");
        }
        return this;
    }
    public audio loop()
    {
        return attr("loop");
    }
    public audio loop(boolean loop)
    {
        if (loop)
        {
            return attr("loop");
        }
        return this;
    }
    public audio muted()
    {
        return attr("muted");
    }
    public audio muted(boolean muted)
    {
        if (muted)
        {
            return attr("muted");
        }
        return this;
    }
    public audio preload(preload preload)
    {
        return attr("preload",preload);
    }
    public audio src(String URL)
    {
        return attr("src",URL);
    }

}
