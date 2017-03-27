package com.geneva.lexing3;

public class TextPosition
{
	final String lineText;
	final int line;
	final int column;
	
	public TextPosition(Lexeme lexeme)
	{
		String text=lexeme.getSource().getText();

		int line=0;
		String lineText="";
		int lineStart=0;
		for (int i=0;i<text.length();i++)
		{
			char c=text.charAt(i);
			if (c=='\n')
			{
				line++;
				if (i>0)
				{
					if (text.charAt(i-1)=='\r')
					{
						lineText=text.substring(lineStart, i-1);
					}
				}
				else
				{
					lineText=text.substring(lineStart, i);
				}
				if (i>=lexeme.getStart())
				{
					break;
				}
				lineStart=i+1;
			}
		}
		this.lineText=lineText;
		this.line=line;
		this.column=lexeme.getStart()-lineStart;
	}

	public String getLineText()
	{
		return lineText;
	}

	public int getLine()
	{
		return line;
	}

	public int getColumn()
	{
		return column;
	}

}
