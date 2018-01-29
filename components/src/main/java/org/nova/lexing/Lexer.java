package org.nova.lexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Lexer
{
    class OperatorTree
    {
        private HashMap<Character,OperatorTree> leaves;
        
        public OperatorTree()
        {
            this.leaves=null;
        }

        public OperatorTree getLeave(char c)
        {
            if (this.leaves==null)
            {
                return null;
            }
            return this.leaves.get(c);
        }

        public boolean isEnd()
        {
            return this.leaves==null;
        }
        
        public void add(String s)
        {
            build(s.toCharArray(),0);
        }
        
        
        private void build(char[] array,int index)
        {
            char c=array[index];
            if (this.leaves==null)
            {
                this.leaves=new HashMap<>();
            }
            OperatorTree childNode=this.leaves.get(c);
            if (childNode==null)
            {
                childNode=new OperatorTree();
                this.leaves.put(c, childNode);
            }
            if (index<array.length-1)
            {
                childNode.build(array, index+1);;
            }
        }
    }
    
    final private HashSet<String> separators;
    final private HashSet<String> keywords;
    final private Source source;
    
    final private String text;
    final private int length;
    private int position;
    private int mark;
    
    private ArrayList<Lexeme> lexemes;
    private ArrayList<Extra> comments;
    private ArrayList<Extra> extras;
    private final OperatorTree operatorTree;
    
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
        this.operatorTree=new OperatorTree();
        for (String operator:operators)
        {
            this.operatorTree.add(operator);
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

    private String matchOperator(char c)
    {
        OperatorTree node=this.operatorTree.getLeave(c);
        if (node==null)
        {
            return null;
        }
        StringBuilder sb=new StringBuilder(c);
        while (node.isEnd()==false)
        {
            c=nextCharacter();
            node=node.getLeave(c);
            if (node==null)
            {
                back(1);
                break;
            }
            sb.append(c);
        }
        return sb.toString();
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
            String operator=matchOperator(c);
            if (operator!=null)
            {
                this.lexemes.add(produceLexeme(Token.OPERATOR,operator,0));
                continue;
            }
            
        }
    }

    private Lexeme produceLexeme(Token token,String value,int back)
    {
        return new Lexeme(this.source,this.text.substring(this.mark, this.position-back),token,value,this.mark);
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
        this.comments.add(new Extra(this.source,ExtraNote.SINGLE_LINE_COMMENT,this.mark,this.position));
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
                        this.comments.add(new Extra(this.source,ExtraNote.MULTI_LINE_COMMENT,this.mark,this.position));
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
        this.comments.add(new Extra(this.source,ExtraNote.UNTERMINATED_COMMENT,this.mark,this.text.length()));
    }
    
}
