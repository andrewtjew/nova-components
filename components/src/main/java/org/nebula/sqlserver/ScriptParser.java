package org.nebula.sqlserver;

import java.util.ArrayList;

import org.nova.scan.Lexeme;
import org.nova.scan.LexerException;
import org.nova.scan.TextSource;

public class ScriptParser
{
    final private ArrayList<Function> functions; 
    final private ArrayList<Procedure> procedures; 
    final private ArrayList<Table> tables ;
    final private String text;
    
    private ScriptParser(String text)
    {
        this.text=text;
        this.functions=new ArrayList<>();
        this.procedures=new ArrayList<>();
        this.tables=new ArrayList<>();
    }
    
    private SqlServerObjects process() throws Throwable
    {
        parse();
        return new SqlServerObjects(this.functions.toArray(
                new Function[this.functions.size()])
               ,this.procedures.toArray(new Procedure[this.procedures.size()])
               ,this.tables.toArray(new Table[this.tables.size()])
        );
    }

    static final String MARKER="\nGO";
    static final String BEGIN_COMMENT="/*";
    static final String END_COMMENT="*/";
    
    private void parse() throws Throwable
    {
        String[] blocks=this.text.split(MARKER);
        for (String block:blocks)
        {
            TextSource source=new TextSource(block);
            SqlServerLexer lexer=new SqlServerLexer(source);
            lexer.skipComments();
            if (lexer.begin()==0)
            {
                continue;
            }
            lexer.end(1);
            Lexeme lexeme=lexer.expectWord();
            if (lexeme.isError())
            {
                throw new LexerException("Block verb expected", lexeme);
            }
            if (lexeme.isCaseInsenstiveWord("USE"))
            {
                continue;
            }
            if (lexeme.isCaseInsenstiveWord("ALTER"))
            {
                continue;
            }
            if (lexeme.isCaseInsenstiveWord("IF"))
            {
                continue;
            }
            if (lexeme.isCaseInsenstiveWord("SET"))
            {
                continue;
            }
            if (lexeme.isCaseInsenstiveWord("CREATE"))
            {
                parse_CREATE(lexer,block);
                continue;
            }
            System.out.println(lexeme.getValue());
        }
    }
    
    
    void parse_CREATE(SqlServerLexer lexer,String block) throws Throwable
    {
        Lexeme lexeme=lexer.expectWord();
        if (lexeme.isError())
        {
            throw new LexerException("Object name expected", lexeme);
        }
        if (lexeme.isCaseInsenstiveWord("TABLE"))
        {
            parse_CREATE_TABLE(lexer,block);
            return;
        }
        if (lexeme.isCaseInsenstiveWord("FUNCTION"))
        {
            parse_CREATE_FUNCTION(lexer,block);
            return;
        }
        if (lexeme.isCaseInsenstiveWord("PROCEDURE"))
        {
            parse_CREATE_PROCEDURE(lexer,block);
            return;
        }
    }
    
    void parse_CREATE_FUNCTION(SqlServerLexer lexer,String block) throws Throwable
    {
        Lexeme owner=null;
        Lexeme name=lexer.expectWordWithBracketsRemoved();
        char c=lexer.skipWhiteSpaceAndBegin();
        if (c=='.')
        {
            lexer.end(0);
            owner=name;
            name=lexer.expectWordWithBracketsRemoved();
        }
        Function function=new Function(block,owner==null?null:owner.getValue(), name.getValue(), block);
        this.functions.add(function);
    }
    
    void parse_CREATE_PROCEDURE(SqlServerLexer lexer,String block) throws Throwable
    {
        Lexeme owner=null;
        Lexeme name=lexer.expectWordWithBracketsRemoved();
        char c=lexer.skipWhiteSpaceAndBegin();
        if (c=='.')
        {
            lexer.end(0);
            owner=name;
            name=lexer.expectWordWithBracketsRemoved();
        }
        Procedure procedure=new Procedure(block,owner==null?null:owner.getValue(), name.getValue(), block);
        this.procedures.add(procedure);
    }
    
    void parse_CREATE_TABLE(SqlServerLexer lexer,String block) throws Throwable
    {
        Lexeme owner=null;
        Lexeme tableName=lexer.expectWordWithBracketsRemoved();
        char c=lexer.skipWhiteSpaceAndBegin();
        if (c=='.')
        {
            lexer.end(0);
            owner=tableName;
            tableName=lexer.expectWordWithBracketsRemoved();
        }
        else
        {
            lexer.end(1);
        }
        lexer.expectPunctuator('(');
        ArrayList<Column> columns=new ArrayList<>();
        
        for (c=lexer.skipWhiteSpaceAndBegin();c!=')';)
        {
            if (c!='[')
            {
                lexer.revert();
                Lexeme lexeme=lexer.produceJavaIdentifier();
                if (lexeme.isError())
                {
                    throw new LexerException("type attribute expected", lexeme);
                }
                if (lexeme.isCaseInsenstiveWord("CONSTRAINT"))
                {
                    break;
                }                
                throw new LexerException("???", lexeme);
            }
            lexer.revert();
            Lexeme name=lexer.expectWordWithBracketsRemoved();
            
            Lexeme type=lexer.expectWordWithBracketsRemoved();
            boolean identity=false;
            Lexeme identityStart=null;
            Lexeme identityIncrement=null;
            Lexeme size=null;
            boolean nullAllowed=true;
            c=lexer.skipWhiteSpaceAndBegin();
            if (c=='(')
            {
                lexer.skipWhiteSpaceAndBegin();
                size=lexer.produceNumber(false);
                Lexeme lexeme=lexer.expectPunctuator(')');
                if (lexeme.isError())
                {
                    throw new LexerException("type size expected", lexeme);
                }
                c=lexer.skipWhiteSpaceAndBegin();
            }
            if ((c!=',')&&(c!=')'))
            {
                Lexeme lexeme=lexer.produceJavaIdentifier();
                if (lexeme.isError())
                {
                    throw new LexerException("type attribute expected", lexeme);
                }
                if (lexeme.isCaseInsenstiveWord("IDENTITY"))
                {
                    lexeme=lexer.expectPunctuator('(');
                    if (lexeme.isError())
                    {
                        throw new LexerException("( expected", lexeme);
                    }
                    lexer.skipWhiteSpaceAndBegin();
                    identityStart=lexer.produceNumber(false);
                    if (identityStart.isError())
                    {
                        throw new LexerException("number expected", lexeme);
                    }
                    lexeme=lexer.expectPunctuator(',');
                    if (identityStart.isError())
                    {
                        throw new LexerException(", expected", lexeme);
                    }
                    lexer.skipWhiteSpaceAndBegin();
                    identityIncrement=lexer.produceNumber(false);
                    if (identityIncrement.isError())
                    {
                        throw new LexerException("number expected", lexeme);
                    }
                    lexeme=lexer.expectPunctuator(')');
                    if (lexeme.isError())
                    {
                        throw new LexerException(") expected", lexeme);
                    }
                    lexer.skipWhiteSpaceAndBegin();
                    lexeme=lexer.produceJavaIdentifier();
                    if (lexeme.isError())
                    {
                        throw new LexerException("type attribute expected", lexeme);
                    }
                }
                if (lexeme.isCaseInsenstiveWord("NOT"))
                {
                    nullAllowed=false;
                    lexer.skipWhiteSpaceAndBegin();
                    lexeme=lexer.produceJavaIdentifier();
                }
                if (lexeme.isCaseInsenstiveWord("NULL")==false)
                {
                    throw new LexerException("NULL expected", lexeme);
                }
                c=lexer.skipWhiteSpaceAndBegin();
            }
            Column column=new Column(name.getValue(), type.getValue(), size==null?0:Integer.parseInt(size.getValue()), identity,identityStart==null?0:Long.parseLong(identityStart.getValue()), identityIncrement==null?0:Long.parseLong(identityIncrement.getValue()), nullAllowed);
            columns.add(column);
            if (c==',')
            {
                lexer.end(0);
                c=lexer.skipWhiteSpaceAndBegin();
                continue;
            }
          
            
        }
        lexer.end(0);
        Table table=new Table(block, owner==null?null:owner.getValue(),tableName.getValue(), columns.toArray(new Column[columns.size()]));
        this.tables.add(table);
    }
    
    static public SqlServerObjects parse(String text) throws Throwable
    {
        ScriptParser parser=new ScriptParser(text);
        return parser.process();
    }
}

