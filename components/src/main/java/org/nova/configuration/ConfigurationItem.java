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
package org.nova.configuration;

public class ConfigurationItem
{
	final private String name;
	final private String description;
	final private String value;
	final private String sourceContext;
	final private ConfigurationSource source;
	public ConfigurationItem(String name,String value,ConfigurationSource source,String sourceContext,String description)
	{
//		System.out.println(source);
		this.name=name;
		this.description=description;
		this.value=value;
		this.sourceContext=sourceContext;
		this.source=source;
	}
	public String getName()
	{
		return this.name;
	}
	public String getDescription()
	{
		return description;
	}
	public String getValue()
	{
		return value;
	}
	public String getSourceContext()
	{
		return sourceContext;
	}
	public ConfigurationSource getSource()
	{
		return source;
	}
	
	
}
