package org.nova.html.widgets.templates;

import java.util.HashMap;
import java.util.List;

import org.nova.collections.FileCache;
import org.nova.concurrent.Lock;
import org.nova.concurrent.LockManager;
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
	
	public Template get(String htmlFile) throws Throwable
	{
		synchronized (this)
		{
			Section[] sections=this.templates.get(htmlFile);
			if (sections!=null)
			{
				return new Template(sections);
			}
		}
		try (Lock<String> lock=this.lockManager.waitForLock(htmlFile))
		{
			synchronized (this)
			{
				Section[] sections=this.templates.get(htmlFile);
				if (sections!=null)
				{
					return new Template(sections);
				}
			}
			Parser parser=new Parser();
			Section[] sections=parser.parse(this.baseDirectory+htmlFile);
			synchronized (this)
			{
				this.templates.put(htmlFile,sections);
			}
			return new Template(sections);
		}
	}
	
	

}
