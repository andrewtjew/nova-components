package org.nova.html.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseContext
{
	final String baseDirectory;
	final ElementMap parameters;
    final ArrayList<Section> sections;
    StringBuilder sectionText;

    public ParseContext(String baseDirectory,ElementMap templateParameterMap)    
    {
        this.sections=new ArrayList<>();
        this.parameters=templateParameterMap;
        this.baseDirectory=baseDirectory;
        this.sectionText=new StringBuilder();
    }
    void addSection(String key)
    {
    	if (sectionText.length()>0)
    	{
    		this.sections.add(new ContentSection(sectionText.toString()));
    		sectionText=new StringBuilder();
    	}
    	this.sections.add(new InsertSection(key));
    }
    
    List<Section> done()
    {
    	if (sectionText.length()>0)
    	{
    		this.sections.add(new ContentSection(sectionText.toString()));
    	}
    	return sections;
    }
    
}
