package org.nova.lexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Lexer
{
    static class Operator
    {
        final char[] letters;
        final String value;
        Operator(String value)
        {
            this.value=value;
            if (value.length()>1)
            {
                this.letters=value.substring(1).toCharArray();
            }
            else
            {
                this.letters=null;
            }
        }
        boolean match(char[] additionals,int additionalsLength)
        {
            if (this.letters==null)
            {
                return true;
            }
            for (int i=0;i<additionalsLength;i++)
            {
                if (this.letters[i]!=additionals[i])
                {
                    return false;
                }
                        
            }
            return true;
        }
        boolean isSingle()
        {
            return this.letters==null;
        }
    }
    
    final private HashSet<String> separators;
    final private HashMap<Character,ArrayList<Operator>> operarators;
    final private HashSet<String> keywords;
    final private Source source;
    
    final private String text;
    final private int length;
    final private char[] operatorCharacters;
    private int position;
    private int mark;
    
    private ArrayList<Lexeme> lexemes;
    private ArrayList<Fragment> comments;
    private ArrayList<Fragment> extras;
    
    
    public Lexer(Source source,String[] separators,String[] operators,String[] keywords)
    {
        this.source=source;
        this.text=source.getText();
        this.length=this.text.length();
        this.separators=new HashSet<>();
        for (String separator:separators)
        {
            this.separators.add(separator);
        }
        this.operarators=new HashMap<>();
        int longest=0;
        for (String operator:operators)
        {
            char start=operator.charAt(0);
            ArrayList<Operator> list=this.operarators.get(start);
            if (list==null)
            {
                list=new ArrayList<>();
                this.operarators.put(start, list);
            }
            list.add(new Operator(operator));
            if (operator.length()>longest)
            {
                longest=operator.length();
            }
        }
        if (longest>1)
        {
            this.operatorCharacters=new char[longest-1];
        }
        else
        {
            this.operatorCharacters=null;
        }
        
        this.keywords=new HashSet<>();
        for (String keyword:keywords)
        {
            this.keywords.add(keyword);
        }
        this.lexemes=new ArrayList<>();
        this.extras=new ArrayList<>();
        this.comments=new ArrayList<>();
    }

    private char nextCharacter()
    {
        if (this.position>=length)
        {
            return 0;
        }
        return this.text.charAt(this.position++);
    }
    
    private char getNextNonspaceCharacterAndMark()
    {
        for (char c=nextCharacter();c!=0;c=nextCharacter())
        {
            if (Character.isWhitespace(c)==false)
            {
                if (c!=65279) //BOM or zero width space
                {
                    this.mark=this.position;
                    return c;
                }
            }
        }
        return 0;
        
    }
    
    private void revertToMark(int offset)
    {
        this.position=this.mark+offset;
    }
    private void back(int amount)
    {
        this.position-=amount;
    }
    
    
    public void process()
    {
        for (char c=getNextNonspaceCharacterAndMark();c!=0;c=getNextNonspaceCharacterAndMark())
        {
            if (c=='/')
            {
                char next=nextCharacter();
                if (next=='*')
                {
                    produceNestableSlashStarComment();
                    continue;
                }
                else if (next=='/')
                {
                    produceSlashSlashComment();
                    continue;
                }
            }
            if (this.separators.contains(c))
            {
                this.lexemes.add(produceLexeme(Token.SEPARATOR,Character.toString(c),0));
                continue;
            }
            List<Operator> operators=this.operarators.get(c);
            if (operators!=null)
            {
                this.operatorCharacters[0]=c;
                Operator best=null;
                for (Operator operator:operators)
                {
                    if (operator.isSingle())
                    {
                        best=operator;
                    }
                }
                for (int i=1;i<this.operatorCharacters.length;i++)
                {
                    c=nextCharacter();
                    if (c==0)
                    {
                        break;
                    }
                    this.operatorCharacters[i]=c;
                    boolean anyMatches=false;
                    for (Operator operator:operators)
                    {
                        if (operator.match(this.operatorCharacters, i+1))
                        {
                            best=operator;
                            anyMatches=true;
                        }
                    }
                    if (anyMatches==false)
                    {
                        break;
                    }
                }
                this.lexemes.add(produceLexeme(Token.OPERATOR,new String(this.operatorCharacters,0,length),0));
            }
        }
    }

    private Lexeme produceLexeme(Token token,String value,int back)
    {
        return new Lexeme(this.source,this.text.substring(this.mark, this.position-back),token,value,this.mark);
    }
    
    private Fragment produceFragment(int forward,int back)
    {
        return new Fragment(this.source,this.text.substring(this.mark+forward, this.position-back),this.mark);
        
    }
    private Fragment produceFragment(int back)
    {
        return new Fragment(this.source,this.text.substring(this.mark, this.position-back),this.mark);
        
    }
    
    private void produceSlashSlashComment() 
    {
        for (char c=nextCharacter();c!=0;c=nextCharacter())
        {
            if ((c == '\r')||(c=='\n'))
            {
                break;
            }
        }
        this.comments.add(produceFragment(0));
    }
    public void produceNestableSlashStarComment() 
    {
        boolean insideString = false;
        int level = 0;
        for (char c=nextCharacter();c!=0;c=nextCharacter())
        {
            if (insideString)
            {
                if (c == '"')
                {
                    insideString = false;
                }
                else if (c == '\\')
                {
                    c = nextCharacter();
                }
            }
            else if (c == '"')
            {
                insideString = true;
            }
            else if (c == '*')
            {
                c = nextCharacter();
                if (c == '/')
                {
                    if (level == 0)
                    {
                        this.comments.add(produceFragment(-1));
                        return;
                    }
                    level--;
                }
            }
            else if (c == '/')
            {
                c = nextCharacter();
                if (c == '*')
                {
                    level++;
                }
            }
        }
        this.extras.add(produceFragment(0));
    }
    
}
