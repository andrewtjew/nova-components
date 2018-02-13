package org.nova.parsing.section;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.nova.lexing.Lexeme;
import org.nova.lexing.Token;
import org.nova.parsing.expression.ExpressionNode;
import org.nova.parsing.expression.ParseError;

public class SectionParser
{
    final private HashSet<String> startKeywords;
    
    public SectionParser(String[] startKeywords)
    {
        this.startKeywords=new HashSet<>();
        for (String startKeyword:startKeywords)
        {
            this.startKeywords.add(startKeyword);
        }
    }
    public List<Section> parse(int start,int end,List<Lexeme> lexemes)
    {
        ArrayList<Section> sections=new ArrayList<>();
        Lexeme lexeme=lexemes.get(start);
        if (lexeme.getToken()!=Token.KEYWORD)
        {
            return null;
        }        
        int sectionStart=start;
        for (int i=start+1;i<end;i++)
        {
            lexeme=lexemes.get(i);
            if ((lexeme.getToken()==Token.KEYWORD)&&(this.startKeywords.contains((String)lexeme.getValue())))
            {
                sections.add(new Section(sectionStart,i));
                sectionStart=i;
            }
        }
        if (sectionStart<end-1)
        {
            sections.add(new Section(sectionStart,end));
        }
        return sections;
    }
}
