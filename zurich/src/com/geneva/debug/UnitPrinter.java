package com.geneva.debug;
import java.io.PrintStream;
import java.util.List;

import com.geneva.parsing.Modifiers;
import com.geneva.parsing.MutableNameType;
import com.geneva.parsing.MutableNameTypeExpression;
import com.geneva.parsing.NameType;
import com.geneva.parsing.NameTypeExpression;
import com.geneva.parsing.Namespace;
import com.geneva.parsing.Node;
import com.geneva.parsing.Unit;
import com.geneva.parsing.member.ClassMember;
import com.geneva.parsing.member.EnumMember;
import com.geneva.parsing.member.FieldMember;
import com.geneva.parsing.member.FunctionMember;
import com.geneva.parsing.member.Member;
import com.geneva.parsing.member.NamespaceMember;
import com.geneva.parsing.member.UsingMember;
import com.geneva.parsing.statement.BreakStatement;
import com.geneva.parsing.statement.CatchStatement;
import com.geneva.parsing.statement.ContinueStatement;
import com.geneva.parsing.statement.ElseIfStatement;
import com.geneva.parsing.statement.ElseStatement;
import com.geneva.parsing.statement.ExpressionStatement;
import com.geneva.parsing.statement.FinallyStatement;
import com.geneva.parsing.statement.GotoStatement;
import com.geneva.parsing.statement.IfStatement;
import com.geneva.parsing.statement.LabelStatement;
import com.geneva.parsing.statement.Statement;
import com.geneva.parsing.statement.TryStatement;
import com.geneva.parsing.statement.UsingStatement;
import com.geneva.parsing.statement.WhileStatement;
import com.geneva.parsing.type.AlternativeType;
import com.geneva.parsing.type.FunctionType;
import com.geneva.parsing.type.RecordType;
import com.geneva.parsing.type.SimpleType;
import com.geneva.parsing.type.Type;

public class UnitPrinter
{
	static public void print(Unit unit) throws Exception
	{
		print(unit, System.out);
	}

	static public void print(Unit unit, PrintStream stream) throws Exception
	{
		UnitPrinter printer = new UnitPrinter(stream);
		printer.printUnit(unit);
	}

	final private PrintStream stream;
	private int level;

	private UnitPrinter(PrintStream stream)
	{
		this.stream = stream;
	}

	private void printIndent()
	{
		for (int i = 0; i < level; i++)
		{
			stream.print("  ");
		}
	}

	private void printTermination()
	{
		stream.print(";");
	}

	private void print(Object... args)
	{
		for (Object object : args)
		{
			stream.print(object.toString());
		}
	}

	private void println(Object... args)
	{
		for (Object object : args)
		{
			stream.print(object.toString());
		}
		stream.println();
	}

	private void printMembersWithIndentation(List<Member> members)
	{
		printIndent();
		println("{");
		level++;
		printMembers(members);
		level--;
		printIndent();
		println("}");
	}

	private void printNameType(NameType nameType)
	{
		print(nameType.getName().getValue());
		if (nameType.getType()!=null)
		{
			print(":");
			printType(nameType.getType());
		}
	}
	private void printMutableNameType(MutableNameType nameType)
	{
		if (nameType.isMutable())
		{
			print("mutable ");
		}
		print(nameType.getName().getValue());
		if (nameType.getType()!=null)
		{
			print(":");
			printType(nameType.getType());
		}
	}
	
	private void printType(Type type)
	{
		if (type instanceof SimpleType)
		{
			print(((SimpleType)type).getNamespace().getValue());	
		}
		else if (type instanceof FunctionType)
		{
			FunctionType t=(FunctionType)type;
			print("(");
			boolean first=true;
			for (MutableNameType item:t.getInputTypes())
			{
				if (first==false)
				{
					print(",");
				}
				else
				{
					first=false;
				}
				printMutableNameType(item);
			}
			print(")");
			if (t.getOutputType()!=null)
			{
				print("=>");
				printType(t.getOutputType());
			}
		}
		else if (type instanceof RecordType)
		{
			RecordType t=(RecordType)type;
			print("{");		
			boolean first=true;
			for (NameType nameType:t.getNameTypes())
			{
				if (first==false)
				{
					print(",");
				}
				else
				{
					first=false;
				}
				print(nameType.getName().getValue());
				if (nameType.getType()!=null)
				{
					print(":");
					printType(nameType.getType());
				}
			}
			print("}");		
		}
		else if (type instanceof AlternativeType)
		{
			AlternativeType t=(AlternativeType)type;
			boolean first=true;
			for (Type alternative:t.getAlternatives())
			{
				if (first==false)
				{
					print("|");
				}
				else
				{
					first=false;
				}
				printType(alternative);
			}
		}
	}

	private void printNameTypeExpression(NameTypeExpression nameTypeExpression)
	{
		
		printNameType(nameTypeExpression.getNameType());
		if (nameTypeExpression.getExpression()!=null)
		{
    		print("=");
    		printExpression(nameTypeExpression.getExpression());
		}
	}
	private void printMutableNameTypeExpression(MutableNameTypeExpression nameTypeExpression)
	{
		
		printMutableNameType(nameTypeExpression.getMutableNameType());
		if (nameTypeExpression.getExpression()!=null)
		{
    		print("=");
    		printExpression(nameTypeExpression.getExpression());
		}
	}
	

	private void printInputParameters(List<MutableNameType> parameters)
	{
		print("(");
		for (MutableNameType parameter:parameters)
		{
			printMutableNameType(parameter);
		}
		print(")");
	}
	private void printOutputParameters(List<NameType> parameters)
	{
		print("(");
		for (NameType parameter:parameters)
		{
			printNameType(parameter);
		}
		print(")");
	}
	private void printMembers(List<Member> members)
	{
		for (Member member : members)
		{
			if (member instanceof ClassMember)
			{
				ClassMember m = (ClassMember) member;
				printIndent();
				if (m.isPublic())
				{
					print("public ");
				}
				print("class ");
				print(m.getName().getValue());
				Namespace superClass=m.getSuperClass();
				if (superClass!=null)
				{
					print(" extends ");
					print(superClass.getValue());
				}
				println();
				printMembersWithIndentation(m.getMembers());

			}
			if (member instanceof NamespaceMember)
			{
				NamespaceMember m = (NamespaceMember) member;
				printIndent();
				print("namespace ");
				print(m.getNamespace().getValue());
				println();
				printMembersWithIndentation(m.getMembers());

			}
			else if (member instanceof EnumMember)
			{
				EnumMember m = (EnumMember) member;
				printIndent();
				if (m.isPublic())
				{
					print("public ");
				}
				print("enum ");
				print(m.getName().getValue());
				Node type=m.getType();
				if (type!=null)
				{
					print(":");
					printExpression(type);
				}
				print("{");
				printExpression(m.getElements());
				println("}");

			}
			else if (member instanceof UsingMember)
			{
				UsingMember m = (UsingMember) member;
				printIndent();
				print("using ");
				if (m.getAlias()!=null)
				{
					print(m.getAlias().getValue());
					print("=");
				}
				print(m.getNamespace().getValue());
				println(";");

			}
			else if (member instanceof FieldMember)
			{
				FieldMember m = (FieldMember) member;
				if (m.getAttributes() != null)
				{
					printIndent();
					printAttributes(m.getAttributes());
					println();
				}
				printIndent();
				printModifiers(m.getModifiers());
				printMutableNameTypeExpression(m.getMutableNameTypeExpression());
				println(";");
			}
			else if (member instanceof FunctionMember)
			{
				FunctionMember m = (FunctionMember) member;
				if (m.getAttributes() != null)
				{
					printIndent();
					printAttributes(m.getAttributes());
					println();
				}
				printIndent();
				printModifiers(m.getModifiers());
				print(m.getName().getValue());
				printInputParameters(m.getInputParameters());
				/*
				if (m.getOutputParameters()!=null)
				{
					print("=>");
					printOutputParameters(m.getOutputParameters());
				}
				*/
				if (m.getOutputType()!=null)
				{
					print("=>");
					printType(m.getOutputType());
				}
				if (m.getStatements()==null)
				{
					println(";");
				}
				else
				{
					println();
					printStatementsWithIndentation(m.getStatements());
					println();
				}
			}
		}
	}
	
	private void printStatementsWithIndentation(List<Statement> statements)
	{
		printIndent();
		println("{");
		level++;
		for (Statement statement:statements)
		{
			if (statement instanceof IfStatement)
			{
				IfStatement s=(IfStatement)statement;
				printIndent();
				print("if (");
				printExpression(s.getExpression());
				println(")");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof UsingStatement)
			{
				UsingStatement s=(UsingStatement)statement;
				printIndent();
				print("using (");
				boolean first=true;
				for (NameTypeExpression item:s.getNameTypeExpressions())
				{
					if (first==false)
					{
						print(",");
					}
					else
					{
						first=false;
					}
					printNameTypeExpression(item);
				}
				println(")");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof ElseIfStatement)
			{
				ElseIfStatement s=(ElseIfStatement)statement;
				printIndent();
				print("else if (");
				printExpression(s.getExpression());
				println(")");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof TryStatement)
			{
				TryStatement s=(TryStatement)statement;
				printIndent();
				println("try");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof CatchStatement)
			{
				CatchStatement s=(CatchStatement)statement;
				printIndent();
				if (s.getMutableNameTypes()==null)
				{
					println("catch");
				}
				else
				{
    				print("catch (");
    				boolean first=true;
    				for (MutableNameType item:s.getMutableNameTypes())
    				{
    					if (first==false)
    					{
    						print(",");
    					}
    					else
    					{
    						first=false;
    					}
    					printMutableNameType(item);
    				}
    				println(")");
				}
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof FinallyStatement)
			{
				FinallyStatement s=(FinallyStatement)statement;
				printIndent();
				println("finally");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof ElseStatement)
			{
				ElseStatement s=(ElseStatement)statement;
				printIndent();
				println("else");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof WhileStatement)
			{
				WhileStatement s=(WhileStatement)statement;
				printIndent();
				print("while (");
				printExpression(s.getExpression());
				println(")");
				printStatementsWithIndentation(s.getStatements());
				println();
			}
			else if (statement instanceof BreakStatement)
			{
				printIndent();
				println("break;");
			}
			else if (statement instanceof ContinueStatement)
			{
				printIndent();
				println("continue;");
			}
			else if (statement instanceof GotoStatement)
			{
				GotoStatement s=(GotoStatement)statement;
				printIndent();
				print("goto ");
				println(s.getLabel().getValue(),";");
				
			}
			else if (statement instanceof LabelStatement)
			{
				LabelStatement s=(LabelStatement)statement;
				print("label ");
				println(s.getLabel().getValue(),";");
				
			}
			else if (statement instanceof ContinueStatement)
			{
				printIndent();
				println("continue;");
			}
			else if (statement instanceof ExpressionStatement)
			{
				printExpressionStatement((ExpressionStatement)statement);
			}
		}
		level--;
		printIndent();
		print("}");	
	}
	private void printExpressionStatement(ExpressionStatement statement)
	{
		printIndent();
		printMutableNameTypeExpression(statement.getMutableNameTypeExpression());
		print(";");
		println();
	}
	
	private void printAttributes(List<NameTypeExpression> attributes)
	{
		print("[");
		for (NameTypeExpression item:attributes)
		{
			printNameTypeExpression(item);
		}
		print("]");
	}
	private void printModifiers(Modifiers modifiers)
	{
		if (modifiers!=null)
		{
			if (modifiers.isPublic())
			{
				print("public ");
			}
		}
	}

	private void printUnit(Unit unit) throws Exception
	{
		printMembers(unit.getMembers());
	}
	
	private void printExpression(Node node)
	{
//		printInfix(node);
		printPrefix(node);
	}
	
	private void printInfix(Node node)
	{
		if (node==null)
		{
			return;
		}
		switch (node.getType())
		{
		case TERMINAL:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("TM~");
				}
			}
			print(node.getElement().getLexeme().getValue());
			break;

		case PARAMETERS:
			print("(");
			printInfix(node.getLeft());
			print(")");
			printInfix(node.getRight());
			break;

		case FUNCTION:
			if (node.getLeft()==null)
			{
				if (Debug.DEBUG)
				{
					if (Debug.printType)
					{
						print("FN~");
					}
				}

				stream.print(node.getElement().getLexeme().getValue());
				stream.print("(");
				printInfix(node.getRight());
				stream.print(")");
			}
			else
			{
				if (Debug.DEBUG)
				{
					if (Debug.printType)
					{
						print("IN~");
					}
				}
				String value=node.getElement().getLexeme().getValue();
				boolean skip=value.equals(",")||value.equals("=")||value.equals("==")||value.equals(".")||value.equals(":");
				if (skip==false)
				{
					stream.print("(");
				}
				printInfix(node.getLeft());
				stream.print(node.getElement().getLexeme().getValue());
				printInfix(node.getRight());
				if (skip==false)
				{
					stream.print(")");
				}
			}
			break;
			
		case STATEMENT_BLOCK:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("SB~");
				}
			}
			printInfix(node.getRight());
			println();
			printStatementsWithIndentation(node.getElement().getStatements());
			break;
		case LOOKUP:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("LU~");
				}
			}

			printInfix(node.getLeft());
			print("[");
			printInfix(node.getRight());
			print("]");
			break;

		case MAYBE_CONVERSION:
			break;

		}
	}
	private void printPrefix(Node node)
	{
		if (node==null)
		{
			return;
		}
		switch (node.getType())
		{
		case TERMINAL:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("TM~");
				}
			}
			print(node.getElement().getLexeme().getValue());
			break;

		case PARAMETERS:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("PM~");
				}
			}
			printPrefix(node.getLeft());
			print("(");
			printPrefix(node.getRight());
			print(")");
			break;

		case FUNCTION:
			
			if (node.getLeft()==null)
			{
				if (Debug.DEBUG)
				{
					if (Debug.printType)
					{
						print("FN~");
					}
				}

				stream.print(node.getElement().getLexeme().getValue());
				stream.print("(");
				printPrefix(node.getRight());
				stream.print(")");

			}
			else
			{
				if (Debug.DEBUG)
				{
					if (Debug.printType)
					{
						print("IN~");
					}
				}

				String value=node.getElement().getLexeme().getValue();
				stream.print(node.getElement().getLexeme().getValue());
				stream.print("(");
				printPrefix(node.getLeft());
				stream.print(",");
				printPrefix(node.getRight());
				stream.print(")");
			}
			break;
		case STATEMENT_BLOCK:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("SB~");
				}
			}
			printPrefix(node.getRight());
			printStatementsWithIndentation(node.getElement().getStatements());
			break;
		case LOOKUP:
			if (Debug.DEBUG)
			{
				if (Debug.printType)
				{
					print("LU~");
				}
			}
			printPrefix(node.getLeft());
			print("(");
			printPrefix(node.getRight());
			print(")");
			break;

		case MAYBE_CONVERSION:
			break;
		default:
			break;

		}
	}

}
