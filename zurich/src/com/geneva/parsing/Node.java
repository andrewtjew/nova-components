package com.geneva.parsing;

import java.util.ArrayList;
import java.util.List;

import com.geneva.lexing3.Lexeme;

public class Node
{
    public enum NodeType
    {
    	STATEMENT_BLOCK,
//    	MODIFIER,
    	FUNCTION,
    	LOOKUP,
    	TERMINAL,
    	PARAMETERS,
    	ATTRIBUTES,
    	MAYBE_CONVERSION,
    }
	
	final private Element element;
	final private Node left;
	final private Node right;
	final private NodeType type; 
	public Node(NodeType type,Element element)
	{
		this.type=type;
		this.element=element;
		this.left=this.right=null;
	}
	public Node(NodeType type,Element element,Node left,Node right)
	{
		this.type=type;
		this.element=element;
		this.left=left;
		this.right=right;
	}
	public Node getLeft()
	{
		return left;
	}
	public Node getRight()
	{
		return right;
	}
	public NodeType getType()
	{
		return type;
	}
	public Element getElement()
	{
		return element;
	}
	
}
