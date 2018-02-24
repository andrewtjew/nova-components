package org.nova.html.templates;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.nova.collections.FileCache;
import org.nova.concurrent.Lock;
import org.nova.concurrent.LockManager;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.StringComposer;
import org.nova.html.templates.Parser;
import org.nova.html.templates.Section;
import org.nova.html.templates.Template;
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
