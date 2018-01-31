package org.nova.html.bootstrap.attributes.classes;

public class Class<C>
{
	private StringBuilder sb;
	public Class(String name)
	{
		this.sb=new StringBuilder();
		this.sb.append(name);
	}
	@Override
	public String toString()
	{
	    return this.sb.toString();
	}
	public C add(String name)
	{
		this.sb.append(' ');
		this.sb.append(name);
		return (C)this;
	}

}
