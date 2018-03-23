package com.geneva.lexing3;

import com.geneva.util.Source;

public class GenevaLexer extends Lexer
{
	public static LexerResult lex(Source source, int maxLexemes)
	{
		Lexeme[] lexemes = new Lexeme[maxLexemes];
		int count = 0;
		GenevaLexer lexer = new GenevaLexer(source);
		for (Lexeme lexeme = lexer.next(); lexeme != null; lexeme = lexer.next())
		{
			lexemes[count++] = lexeme;
		}
		return new LexerResult(lexemes, count);
	}

	public GenevaLexer(Source source)
	{
		super(source);
	}
	public GenevaLexer(String text)
	{
		this(new Source(text,null));
	}

	public Lexeme decode(char c,int start) 
	{
		if (Character.isWhitespace(c)) // todo: change to C# whitespace
									   // specs
		{
			/*
			 * if (c=='\n') { this.line++; int start=this.index;
			 * this.index++; return new
			 * Lexeme(Token.END_OF_LINE,this.text.substring
			 * (start,this.index),start,this.index,this.line,this.source); }
			 */
			return null;
		}
		if (c == '"')
		{
			return expectString();
		}
		if (c == '\'')
		{
			return expectCharacter();
		}
		if (c == '\\')
		{
			return expectText();
		}
		if (Character.isJavaIdentifierStart(c))
		{
			return expectJavaStyleIdentifierOrKeyword();
		}
		if (Character.isDigit(c))
		{
			return expectNumber();
		}
		if (c == ';')
		{
			return expectTerminator();
		}
		if (c == '(')
		{
			return expectOpenRoundOperator();
		}
		if (c == ')')
		{
			return expectCloseRoundOperator();
		}
		if (c == '[')
		{
			return expectOpenSquareOperator();
		}
		if (c == ']')
		{
			return expectCloseSquareOperator();
		}
		if (c == '{')
		{
			return expectOpenCurlyOperator();
		}
		if (c == '}')
		{
			return expectCloseCurlyOperator();
		}
		/*
		if (c == ',')
		{
			return expectComma();
		}
		*/
		if (c == '/')
		{
			Lexeme lexeme=expectDivideOperatorOrContinue();
			return lexeme;
		}
		if ((c == '+') || (c == '*') || (c == '-') || (c == '/') || (c == '%') || (c == '.') || (c == '^') || (c == '?') || (c == ':'))
		{
			return expectSimpleOperator();
		}
		if (c == '=')
		{
			return expectAnyEqualStartOperator();
		}
		if (c == '>')
		{
			return expectAnyGreaterStartOperator();
		}
		if (c == '<')
		{
			return expectAnyLessStartOperator();
		}
		if (c == '&')
		{
			return expectAnyAndStartOperator();
		}
		if (c == '|')
		{
			return expectAnyOrStartOperator();
		}
		if (c == '!')
		{
			return expectAnyNotOperator();
		}
		return Lexeme.newErrorToken(this.index, this.index, this.source, "Fix me");
	}

}
