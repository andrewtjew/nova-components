package org.nova.html.pages;

import java.util.HashMap;
import java.util.List;

import org.nova.collections.FileCache;
import org.nova.concurrent.Lock;
import org.nova.concurrent.LockManager;
import org.nova.tracing.TraceManager;

public class PageManager
{
	final private HashMap<String,Section[]> templates;
	final private LockManager<String> lockManager;
	final private String baseDirectory;
	
	public PageManager(TraceManager traceManager,String baseDirectory)
	{
		this.baseDirectory=baseDirectory;
		this.templates=new HashMap<>();
		this.lockManager=new LockManager<String>(traceManager,this.getClass().getSimpleName());
	}
	
	public Page get(String rootHtmlFile,KeyValueMap fileParameters) throws Throwable
	{
		synchronized (this)
		{
			Section[] sections=this.templates.get(rootHtmlFile);
			if (sections!=null)
			{
				return new Page(sections);
			}
		}
		try (Lock<String> lock=this.lockManager.waitForLock(rootHtmlFile))
		{
			synchronized (this)
			{
				Section[] sections=this.templates.get(rootHtmlFile);
				if (sections!=null)
				{
					return new Page(sections);
				}
			}
			ParseContext parseContext=new ParseContext(baseDirectory,fileParameters);
			Parser parser=new Parser(parseContext);
			parser.parse(rootHtmlFile);
			List<Section> list=parseContext.done();
			Section[] sections=list.toArray(new Section[list.size()]);
			synchronized (this)
			{
				this.templates.put(rootHtmlFile,sections);
			}
			return new Page(sections);
		}
	}
	
	

}
