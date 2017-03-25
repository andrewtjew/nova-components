package org.nova.html.pages;

import java.util.ArrayList;
import java.util.List;

import org.nova.core.Utils;

public class Menu
{
	final private ArrayList<MenuItem> items;
	String text;
	
	public Menu()
	{
		this.items=new ArrayList<>();
	}
	public void add(MenuItem item) throws Exception
	{
		/*
		for (MenuItem existing:items)
		{
			if (existing.getNavigation().equals(item.getNavigation()))
			{
				throw new Exception("Navigation clash detected. Navigation="+item.getNavigation());
			}
		}
		*/
		this.items.add(item);
		this.text=null;
	}
	public void add(String navigation,String action) throws Exception
	{
		add(new MenuItem(navigation,action));
		this.text=null; //cause it to be recomputed in toString()
	}
	public ArrayList<MenuItem> getItems()
	{
		return items;
	}
	
	static class Node
	{
		List<Node> list=new ArrayList<>();
		MenuItem item;
		String part;
		
		Node(String part,MenuItem item)
		{
			this.item=item;
			this.part=part;
		}
		Node(String part)
		{
			this(part, null);
		}
	}
	
	public String toString()
	{
		if (this.text==null)
		{
			Node root=new Node("root");
			for (MenuItem item:getItems())
			{
				insertMenuItem(item,root,0,Utils.split(item.getNavigation(),'|'));
			}
			StringBuilder sb=new StringBuilder();
			buildMenuText(sb,root);
			this.text=sb.toString();
		}
		return this.text;
	}
	
	private void buildMenuText(StringBuilder sb,Node node)
	{
		sb.append("<ul>");
		for (Node childNode:node.list)
		{
			sb.append("<li>");
			if (childNode.item!=null)
			{
				sb.append("<a href=\""+childNode.item.getAction()+"\">"+childNode.part+"</a>");
			}
			else
			{
				sb.append("<a>"+childNode.part+"</a>");
			}
			if (childNode.list.size()>0)
			{
				buildMenuText(sb,childNode);
			}
			sb.append("</li>");
		}
		sb.append("</ul>");
	}

	private void insertMenuItem(MenuItem item,Node node,int level,String[] parts)
	{
		String part=parts[level];
		Node childNode=null;
		for (Node n:node.list)
		{
			if (n.part.equals(part))
			{
				childNode=n;
			}
		}
		if (childNode==null)
		{
			if (level==parts.length-1)
			{
				if (item.getAction()!=null)
				{
					node.list.add(new Node(part,item));
				}
				else
				{
					node.list.add(new Node(part));
				}
				return;
			}			
			childNode=new Node(part);
			node.list.add(childNode);
		}
		if (item.getNavigation()!=null)
		{
			String[] navigationParts=Utils.split(item.getNavigation(), '|');
			if (level<navigationParts.length)
			{
				if (part.equals(navigationParts[navigationParts.length-1]))
				{
					childNode.item=item;
				}
			}
		}
		if (level<parts.length-1)
		{
			insertMenuItem(item,childNode,level+1,parts);
		}
	}
	
}
