package com.geneva.lexing3;

import java.util.HashSet;

import com.geneva.util.Source;

public abstract class Lexer
{

	protected final Source source;
	protected int index;
	protected final int length;
	protected final String text;
	protected final HashSet<String> keywords;
	protected final HashSet<Character> simpleOperators;
	protected final HashSet<Character> simplePunctuators;

	public Lexer(Source source)
	{
		this.keywords=new HashSet<String>();
		this.simpleOperators=new HashSet<>();
		this.simplePunctuators=new HashSet<>();
		this.source = source;
		this.index = 0;
		this.text = source.getText();
		this.length = this.text.length();
	}
	
	public void addSimplePunctuators(char...punctuators)
	{
		for (char punctuator:punctuators)
		{
			this.simplePunctuators.add(punctuator);
		}
	}
	public void addPunctuator(char punctuator)
	{
		this.simplePunctuators.add(punctuator);
	}
	public void addSimpleOperators(char...operators)
	{
		for (char operator:operators)
		{
			this.simpleOperators.add(operator);
		}
	}
	public void addOperator(char operator)
	{
		this.simpleOperators.add(operator);
	}
	public void addKeywords(String...keywords)
	{
		for (String keyword:keywords)
		{
			this.keywords.add(keyword);
		}
	}
	public void addKeyword(String keyword)
	{
		this.keywords.add(keyword);
	}
	protected boolean isSimpleOperator(char c)
	{
		return this.simpleOperators.contains(c);
	}
	protected boolean isSimplePunctuator(char c)
	{
		return this.simplePunctuators.contains(c);
	}

	protected abstract Lexeme decode(char c,int start);
	
	public Lexeme next()
	{
		for (; this.index < length; this.index++)
		{
			char c = getNextNonJavaWhiteSpaceCharacter();
			if (c==0)
			{
				break;
			}
			Lexeme lexeme=decode(c,this.index);
			if (lexeme!=null)
			{
				return lexeme;
			}
		}
		return new Lexeme(Token.END,null,this.index,this.index,this.source);
	}
	
	protected boolean isEndOfLine(char c)
	{
		return (c=='\r')||(c=='\n');
	}
	protected Lexeme expectEndOfLineTerminator()
	{
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '\n'))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.TERMINATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.TERMINATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectTerminator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.TERMINATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}

	protected Lexeme expectOpenRoundOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPEN_ROUND, this.text.substring(start, this.index), start, this.index, this.source);
	}

	protected Lexeme expectCloseRoundOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.CLOSE_ROUND, this.text.substring(start, this.index), start, this.index, this.source);
	}
	
	protected Lexeme expectOpenSquareOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPEN_SQUARE, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectCloseSquareOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.CLOSE_SQUARE, this.text.substring(start, this.index), start, this.index,this.source);
	}
	protected Lexeme expectOpenCurlyOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPEN_CURLY, this.text.substring(start, this.index), start, this.index,this.source);
	}
	protected Lexeme expectCloseCurlyOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.CLOSE_CURLY, this.text.substring(start, this.index), start, this.index,this.source);
	}
	/*
	protected Lexeme expectComma()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.COMMA, this.text.substring(start, this.index), start, this.index, this.source);
	}
	*/
	protected Lexeme expectDivideOperatorOrContinue()
	{
		int start = this.index;
		if (this.index < this.length - 1)
		{
			char c = this.text.charAt(this.index + 1);
			if ((c == '*') || (c == '/'))
			{
				this.index += 2;
				return expectComment(start, c);
			}
		}
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectSimpleOperator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectSimplePunctuator()
	{
		int start = this.index;
		this.index++;
		return new Lexeme(Token.PUNCTUATOR, this.text.substring(start, this.index), start, this.index,this.source);
	}
	protected Lexeme expectAnyEqualStartOperator()
	{
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '='))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '>'))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.ASSIGNMENT, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectAnyGreaterStartOperator()
	{
		if (index < length - 1)
		{
			char cc = this.text.charAt(this.index + 1);
			if ((cc == '>') || (cc == '='))
			{
				int start = this.index;
				this.index += 2;
				return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
			}
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectAnyLessStartOperator()
	{
		if (index < length - 1)
		{
			char cc = this.text.charAt(this.index + 1);
			if ((cc == '<') || (cc == '='))
			{
				int start = this.index;
				this.index += 2;
				return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
			}
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectAnyAndStartOperator()
	{
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '&'))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectAnyOrStartOperator()
	{
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '|'))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}
	protected Lexeme expectAnyNotOperator()
	{
		if ((index < length - 1) && (this.text.charAt(this.index + 1) == '='))
		{
			int start = this.index;
			this.index += 2;
			return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
		}
		int start = this.index;
		this.index++;
		return new Lexeme(Token.OPERATOR, this.text.substring(start, this.index), start, this.index, this.source);
	}

	protected Lexeme expectComment(int start, char c)
	{
		if (c == '/')
		{
			for (; this.index < this.length; this.index++)
			{
				c = this.text.charAt(this.index);
				if (c == '\r')
				{
					this.index++;
					if (this.index < this.length)
					{
						c = this.text.charAt(this.index);
					}
					return null;
				}
				else if (c == '\n')
				{
					return null;
				}
			}
		}
		else if (c == '*')
		{
			boolean insideString = false;
			int level = 0;
			for (; this.index < this.length; this.index++)
			{
				c = this.text.charAt(this.index);
				if (insideString)
				{
					if (c == '"')
					{
						insideString = false;
					}
					else if (c == '\\')
					{
						if (this.index >= this.length - 1)
						{
							break;
						}
						c = this.text.charAt(this.index + 1);
						if (c == '"')
						{
							this.index++;
						}
						else if (c=='\\')
						{
							this.index++;
						}
					}
				}
				else if (c == '"')
				{
					insideString = true;
				}
				else if (c == '*')
				{
					if (this.index < this.length - 1)
					{
						c = this.text.charAt(this.index + 1);
						if (c == '/')
						{
							this.index++;
							if (level == 0)
							{
								return null;
							}
							level--;
						}
					}
				}
				else if (c == '/')
				{
					if (this.index < this.length - 1)
					{
						c = this.text.charAt(this.index + 1);
						if (c == '*')
						{
							this.index++;
							level++;
						}
					}
				}
				/*
				else if (c == '\n')
				{
					this.line++;
					this.index++;
					return null;
				}
				*/
			}
		}
		return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
	}

	protected Lexeme expectNumber()
	{
		int start = index;
		for (this.index++; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (Character.isDigit(c) == false)
			{
				if (c=='l'||c=='L'||c=='d'||c=='D'||c=='f'||c=='F')
				{
					this.index++;
					return new Lexeme(Token.NUMBER, this.text.substring(start, this.index-1), this.text.substring(index-1, index), start, this.index, this.source,null);
				}
				return new Lexeme(Token.NUMBER, this.text.substring(start, this.index), null, start, this.index, this.source,null);
			}
		}
		return new Lexeme(Token.NUMBER, this.text.substring(start, this.index), null, start, this.index, this.source,null);
	}

	public Lexeme expectJavaStyleIdentifierOrKeyword()
	{
		int start = this.index;
		for (this.index++; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (Character.isJavaIdentifierPart(c) == false)
			{
				break;
			}
			if (c == '$') // $ should not be part of identifier
			{
				break;
			}
		}
		String value = this.text.substring(start, this.index);
		if (this.keywords.contains(value))
		{
			return new Lexeme(Token.KEYWORD, value, start, this.index, this.source);
		}
		return new Lexeme(Token.IDENTIFIER, value, start, this.index, this.source);
	}

	protected Lexeme expectString()
	{
		StringBuilder sb = null;
		int start = this.index;
		int fragmentStart = start + 1;
		for (this.index++; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (c != '\\')
			{
				if (c == '"')
				{
					this.index++;
					if (sb == null)
					{
						return new Lexeme(Token.STRING, this.text.substring(fragmentStart, this.index - 1), start, this.index, this.source);
					}
					if (fragmentStart < this.index)
					{
						sb.append(this.text.substring(fragmentStart, this.index-1));
					}
					return new Lexeme(Token.STRING, sb.toString(), start, this.index, this.source);
				}
				else if (c == '\n')
				{
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				continue;
			}
			if (sb == null)
			{
				sb = new StringBuilder();
			}
			sb.append(this.text.substring(fragmentStart, this.index));
			this.index++;
			if (this.index >= this.length)
			{
				break;
			}
			c = this.text.charAt(this.index);
			if (c == 'u')
			{
				this.index++;
				if (this.index + 4 >= this.length)
				{
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				for (int i = this.index; i < this.index + 4; i++)
				{
					c = this.text.charAt(this.index);
					if (Character.isDigit(c) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')))
					{
						continue;
					}
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				sb.append((char) Integer.parseInt(this.text.substring(this.index, this.index + 4), 16));
				this.index += 4;
				fragmentStart = this.index;
				continue;
			}
			if (c == 'r')
			{
				sb.append('\r');
			//	return Lexeme.newErrorToken(start, this.index, this.line, this.source, "Fix me");
			}
			else if (c == 'n')
			{
				sb.append('\n');
			//	return Lexeme.newErrorToken(start, this.index, this.line, this.source, "Fix me");
			}
			else if (c == '"')
			{
				sb.append('"');
			//	return Lexeme.newErrorToken(start, this.index, this.line, this.source, "Fix me");
			}
			else if (c == '\\')
			{
				sb.append('\\');
			}
			else if (c == 't')
			{
				sb.append('\t');
			}
			else
			{
				return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
			}
			fragmentStart = this.index + 1;
		}
		return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
	}

	boolean isTextEnd(String terminal)
	{
		int mark = this.index;
		for (int i = 1; i < terminal.length(); i++)
		{
			this.index++;
			if (this.index >= this.length)
			{
				this.index = mark;
				return false;
			}
			if (this.text.charAt(this.index) == terminal.charAt(i) == false)
			{
				this.index = mark;
				return false;
			}
		}
		return true;
	}

	protected Lexeme expectText()
	{
		int start = this.index;
		this.index++;
		if (this.index >= this.length)
		{
			return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		char c = this.text.charAt(this.index);
		if (c != '\\')
		{
			return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		for (this.index++; true; this.index++)
		{
			if (this.index >= this.length)
			{
				return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
			}
			c = this.text.charAt(this.index);
			if (c == '\n')
			{
				break;
			}
			if (Character.isWhitespace(c) == false)
			{
				return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
			}
		}
		String terminal = "\\\\";
		char firstEndCharacter = terminal.charAt(0);
		int textStart = this.index + 1;
		for (this.index++; this.index < this.length; this.index++)
		{
			c = this.text.charAt(this.index);
			if (c == firstEndCharacter)
			{
				int textEnd = this.index;
				if (isTextEnd(terminal) == true)
				{
					return new Lexeme(Token.STRING, this.text.substring(textStart, textEnd), start, this.index, this.source);
				}
			}
		}
		return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
	}

	protected Lexeme expectCharacter()
	{
		char value = 0;
		int start = this.index;
		this.index++;
		if (this.index >= this.length)
		{
			return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		char c = this.text.charAt(this.index);
		if (c != '\\')
		{
			value = c;
			this.index++;
		}
		else
		{
			this.index++;
			if (this.index >= this.length)
			{
				return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
			}
			c = this.text.charAt(this.index);
			if (c == 'u')
			{
				this.index++;
				if (this.index + 4 >= this.length)
				{
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				for (int i = this.index; i < this.index + 4; i++)
				{
					c = this.text.charAt(this.index);
					if (Character.isDigit(c) || ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')))
					{
						continue;
					}
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				value = (char) Integer.parseInt(this.text.substring(this.index, this.index + 4), 16);
				this.index += 4;
			}
			else
			{
				if (c == 'r')
				{
					value = '\r';
				}
				else if (c == 'n')
				{
					value = '\n';
				}
				else if (c == '\\')
				{
					value = '\\';
				}
				else if (c == 't')
				{
					value = '\t';
				}
				else
				{
					return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
				}
				this.index++;
			}
		}
		if (this.index >= this.length)
		{
			return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		c = this.text.charAt(this.index);
		if (c != '\'')
		{
			return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		this.index++;
		return new Lexeme(Token.CHARACTER, String.valueOf(value), start, this.index, this.source);
	}

	public String getRestOfLine()
	{
		int start=this.index;
		for (; this.index < length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (c!='\r')
			{
				continue;
			}
			if (this.index < this.length - 1)
			{
				char c2 = this.text.charAt(this.index + 1);
				if (c2=='\n')
				{
					this.index+=2;
					return this.text.substring(start, this.index-2);
				}
				this.index+=1;
				return this.text.substring(start, this.index-1);
			}
			this.index+=1;
			return this.text.substring(start, this.index-1);
		}		
		return this.text.substring(start, this.index);
	}
	
	protected boolean skipJavaComments()
	{
		int start = this.index;
		if (this.index < this.length - 1)
		{
			char c = this.text.charAt(this.index + 1);
			if ((c == '*') || (c == '/'))
			{
				this.index += 2;
				expectComment(start, c);
				return true;
			}
		}
		return false;
	}

	protected boolean isJavaWhiteSpace(char c)
	{
		return Character.isWhitespace(c);
	}
	protected char getNextNonJavaWhiteSpaceCharacter()
	{
		for (int i=this.index;i<this.length;i++)
		{
			char c=this.text.charAt(i);
			if (Character.isWhitespace(c)==false)
			{
				this.index=i;
				return c;
			}
		}
		return 0;
	}
}
