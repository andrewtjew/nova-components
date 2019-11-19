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
