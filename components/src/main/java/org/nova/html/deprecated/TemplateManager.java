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
/*
package org.nova.html.templatesOld;

import java.util.HashMap;
import org.nova.concurrent.Lock;
import org.nova.concurrent.LockManager;
import org.nova.html.elements.Element;
import org.nova.html.elements.StringComposer;
import org.nova.html.templatesOld.Parser;
import org.nova.html.templatesOld.Section;
import org.nova.html.templatesOld.Template;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class TemplateManager
{
	final private HashMap<String,Section[]> templates;
	final private LockManager<String> lockManager;
	final private String baseDirectory;
	
	public TemplateManager(TraceManager traceManager,String baseDirectory)
	{
		this.baseDirectory=baseDirectory;
		this.templates=new HashMap<>();
		this.lockManager=new LockManager<String>(traceManager,this.getClass().getSimpleName());
	}

	
	//TODO: a faster way is to just walk the elements and look for the Insert to build sections. This way, we don't convert everything to text and we don't need the parser.
	public void add(String key,Element element) throws Throwable
	{
	    StringComposer composer=new StringComposer();
        element.compose(composer);
        String text=composer.getStringBuilder().toString();
        Parser parser=new Parser();
        Section[] sections=parser.parseText(text);
        synchronized (this)
        {
            this.templates.put(key,sections);
        }
	}
	
	public Template get(Trace parent,String key) throws Throwable
	{
		synchronized (this)
		{
			Section[] sections=this.templates.get(key);
			if (sections!=null)
			{
				return new Template(sections);
			}
		}
		try (Lock<String> lock=this.lockManager.waitForLock(parent,key))
		{
			synchronized (this)
			{
				Section[] sections=this.templates.get(key);
				if (sections!=null)
				{
					return new Template(sections);
				}
			}
			Parser parser=new Parser();
			Section[] sections=parser.parseFile(this.baseDirectory+key);
			synchronized (this)
			{
				this.templates.put(key,sections);
			}
			return new Template(sections);

		}
	}
	
	

}
*/
