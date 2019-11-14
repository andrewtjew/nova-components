package org.nova.html;

import java.util.HashMap;

public class ExtionsionToTypeMappings
{
    public static ExtionsionToTypeMappings fromDefault()
    {
        ExtionsionToTypeMappings mappings=new ExtionsionToTypeMappings();
        mappings.addMappings("text/plain", "txt","text");
        mappings.addMappings("text/javascript", "js");
        mappings.addMappings("text/css", "css");
        mappings.addMappings("text/html", "htm","html");
        mappings.addMappings("image/x-icon", "ico");
        mappings.addMappings("image/jpeg", "jpeg","jpg","jpe");
        mappings.addMappings("image/png", "png");
        mappings.addMappings("image/bmp", "bmp","bm");
        return mappings;
    }
    
    final private HashMap<String,String> typeMappings;

	public ExtionsionToTypeMappings()
	{
		this.typeMappings=new HashMap<>();
	}
	
	public void addMappings(String contentType,String...extensions)
	{
		for (String extension:extensions)
		{
			this.typeMappings.put(extension, contentType);
		}
	}
	
	public String getContentType(String file)
	{
		int index=file.lastIndexOf('.');
		if (index<0)
		{
			return null;
		}
		return this.typeMappings.get(file.substring(index+1));
		
	}

}
