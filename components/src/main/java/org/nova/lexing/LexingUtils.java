package org.nova.lexing;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LexingUtils
{
    static public String getTextOf(List<Lexeme> list)
    {
        int start=list.get(0).getTextStart();
        int end=list.get(list.size()-1).getTextEnd();
        return list.get(0).getSource().getText().substring(start, end);
    }
    static public List<List<Lexeme>> split(List<Lexeme> list,char seperator)
    {
        ArrayList<List<Lexeme>> results=new ArrayList<>();
        ArrayList<Lexeme> part=new ArrayList<>();
        for (Lexeme lexeme:list)
        {
            if (lexeme.isPunctuator(seperator))
            {
                if (part.size()>0)
                {
                    results.add(part);
                    part=new ArrayList<>();
                }
            }
            else
            {
                part.add(lexeme);
            }
        }
        if (part.size()>0)
        {
            results.add(part);
        }
        return results;
    }
    
    static class LineAndColumn
    {
        final int line;
        final int column;
        final int targetLineStart;
        final int targetLineEnd;
        public LineAndColumn(String text,int position)
        {
            if (position>text.length())
            {
                position=text.length();
            }
            int line=0;
            int column=0;
            int targetLineStart=0;
            int targetLineEnd=text.length();
            for (int i=0;i<position;i++)
            {
                if (text.charAt(i)=='\n')
                {
                    line++;
                    column=0;
                    targetLineStart=i+1;
                }
                else
                {
                    column++;
                }
            }
            for (int i=position;i<text.length();i++)
            {
                if (text.charAt(i)=='\n')
                {
                    targetLineEnd=i+1;
                    break;
                }
            }
            this.line=line;
            this.column=column;
            this.targetLineStart=targetLineStart;
            this.targetLineEnd=targetLineEnd;
        }
    }
    static public void printLexemeError(Lexeme lexeme) throws Exception
    {
        printLexemeError(System.out,lexeme);
    }    
    static public void printLexemeError(PrintStream stream,Lexeme lexeme)
    {
        String text=lexeme.getSource().getText();
        LineAndColumn lineAndColumn=new LineAndColumn(text, lexeme.getTextStart());
        int line=lineAndColumn.line+1;
        int column=lineAndColumn.column+1;
        String position="("+line+","+column+"):";
        stream.print(position);
        stream.println(lexeme.getValue());
        stream.println(text.substring(lineAndColumn.targetLineStart, lineAndColumn.targetLineEnd));
        for (int i=0;i<column-1;i++)
        {
            stream.print(' ');
        }
        for (int i=lexeme.getTextStart();i<lexeme.getTextEnd();i++)
        {
            stream.print('^');
        }
        stream.println();
    }
}
