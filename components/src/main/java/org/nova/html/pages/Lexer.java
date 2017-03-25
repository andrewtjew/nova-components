package org.nova.html.pages;

import java.util.HashSet;
import com.geneva.util.Source;

import jcifs.http.NetworkExplorer;

public class Lexer
{

	protected final Source source;
	protected int index;
	protected final int length;
	protected final String text;
	private boolean inTag;
	public Lexer(Source source)
	{
		this.source = source;
		this.index = 0;
		this.text = source.getText();
		this.length = this.text.length();
		this.inTag=false;
	}
	
	public Lexeme next()
	{
		for (; this.index < length; this.index++)
		{
			char c = getNextNonWhiteSpaceCharacter();
			if (c==0)
			{
				break;
			}
			if (inTag)
			{
				int start=this.index;
				if (c=='/')
				{
					this.index++;
					return new Lexeme(Token.SLASH, "/", this.index-1, this.index,this.source);
				}
				if (c=='=')
				{
					this.index++;
					return new Lexeme(Token.ASSIGNMENT, "=", this.index-1, this.index,this.source);
				}
				if (c=='>')
				{
					this.inTag=false;
					this.index++;
					return new Lexeme(Token.END_TAG, ">", this.index-1, this.index,this.source);
				}
				if ((c=='"')||(c=='\''))
				{
					return this.expectString(c);
				}
				if (Character.isJavaIdentifierStart(c))
				{
					String identifier=getIdentifier();
					if (identifier==null)
					{
						return Lexeme.newErrorToken(this.index, this.index, this.source, "Fix me");
					}
					return new Lexeme(Token.IDENTIFIER,identifier,start,this.index,this.source);
				}
				return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
			}
			if (c=='<')
			{
			    int start=this.index;
			    this.index++;
			    c=getNextNonWhiteSpaceCharacter();
			    if (c!='!')
			    {
    				this.inTag=true;
    				this.index=start+1;
    				return new Lexeme(Token.BEGIN_TAG, null, start, this.index,this.source);
			    }
			    this.index++;
			    String identifier=getIdentifier();
			    if (identifier==null)
			    {
                    return Lexeme.newErrorToken(this.index, this.index, this.source, "Fix me");
			    }
			    if (identifier.equals("DOCTYPE")==false)
                {
                    return Lexeme.newErrorToken(this.index, this.index, this.source, "Fix me");
                }
			    for (this.index++;this.text.charAt(this.index)!='>';this.index++);
			    this.index++;
                return new Lexeme(Token.DOCTYPE, null, start, this.index,this.source);
			    
			}
			return expectText();

		}
		return new Lexeme(Token.END,null,this.index,this.index,this.source);
	}


	public String getIdentifier()
	{
		char c = getNextNonWhiteSpaceCharacter();
		int start = this.index;
		if (Character.isJavaIdentifierStart(c) == false)
		{
			return null;
		}
		for (this.index++; this.index < this.length; this.index++)
		{
			c = this.text.charAt(this.index);
			if (Character.isJavaIdentifierPart(c) == false)
			{
				break;
			}
		}
		return this.text.substring(start, this.index);
	}
	protected Lexeme skipComment(char commentCharacter)
	{
		int start=this.index;
		this.index++;
		if (this.index>this.text.length())
		{
	        return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		if (this.text.charAt(this.index)!='*')
		{
            return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
		}
		boolean insideString = false;
		int level = 0;
		char stringCharacter;
		for (this.index++; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (insideString)
			{
				if (c == '"')
				{
					insideString = false;
				}
				else if (c == '\\')
				{
					if (this.index < this.length - 1)
					{
						c = this.text.charAt(this.index + 1);
						if (c == '"')
						{
							this.index++;
						}
					}
				}
			}
			else if ((c == '"')||(c=='\''))
			{
				stringCharacter=c;
				insideString = true;
			}
			else if (c == '*')
			{
				if (this.index < this.length - 1)
				{
					c = this.text.charAt(this.index + 1);
					if (c == commentCharacter)
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
			else if (c == commentCharacter)
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
		}
		return Lexeme.newErrorToken(start, this.index, this.source, "Fix me");
	}
	protected Lexeme expectString(char endCharacter)
	{
		StringBuilder sb = null;
		int start = this.index;
		int fragmentStart = start + 1;
		for (this.index++; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if (c != '\\')
			{
				if (c == endCharacter)
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
		StringBuilder sb=new StringBuilder();
		int sectionStart=this.index;
		for (; this.index < this.length; this.index++)
		{
			char c = this.text.charAt(this.index);
			if ((c=='@')||(c=='/'))
			{
			    if (this.index>sectionStart)
			    {
			        sb.append(this.text.substring(sectionStart, this.index));
			    }
				Lexeme error=skipComment(c);
				if (error!=null)
				{
				    return error;
				}
				sectionStart=this.index;
			}
			else if (c=='<')
			{
				break;
			}
		}
        if (this.index>sectionStart)
        {
            sb.append(this.text.substring(sectionStart, this.index));
        }
		return new Lexeme(Token.TEXT, sb.toString(), start, this.index,this.source);
	}

	protected boolean isJavaWhiteSpace(char c)
	{
		return Character.isWhitespace(c);
	}
	protected char getNextNonWhiteSpaceCharacter()
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
