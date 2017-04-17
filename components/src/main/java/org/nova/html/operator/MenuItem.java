package org.nova.html.operator;

public class MenuItem
{
	final private String navigation;
	final private String action;
	
	public MenuItem(String navigation,String action)
	{
		this.navigation=navigation;
		this.action=action;
	}

	public String getNavigation()
	{
		return navigation;
	}

	public String getAction()
	{
		return action;
	}

}
