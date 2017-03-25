package org.nova.html.pages;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.nova.collections.FileCache;
import org.nova.core.Utils;

import com.geneva.util.Source;

/*
 * <server file="file"|includeKey="key">
 * 
 */

public class Parser
{
	final ParseContext parseContext;
    private Lexer lexer;

    public Parser(ParseContext parseContext)
    {
        this.parseContext=parseContext;
    }

    Lexeme back = null;
    Lexeme saved = null;

    private Lexeme next()
    {
        Lexeme current;
        if (this.back != null)
        {
            current = this.back;
            this.back = null;
        } else
        {
            current = this.saved = this.lexer.next();
        }
//        System.out.println(current.getToken() + "=" + current.getLiteral()+"=");
        return current;
    }

    private Lexeme back()
    {
        this.back = this.saved;
        return this.back;
    }

    public void parse(String htmlFile) throws Throwable
    {
        String text = Utils.readTextFile(this.parseContext.baseDirectory+File.separator+htmlFile, StandardCharsets.UTF_8);
        this.lexer = new Lexer(new Source(text, htmlFile));
        parse();
    }

    /*
     * <server onClick="send=id[:attribute],id2 ;receive=
     * <replace >
     * 
     * 
     */
    
    void process() throws Throwable
    {
        for (;;)
        {
            Lexeme lexeme = next();
            if (lexeme.isToken(Token.SLASH))
            {
                lexeme = next();
                if (lexeme.isToken(Token.END_TAG) == false)
                {
                    throw new Exception();
                }
                return;
            }
            if (lexeme.isToken(Token.END_TAG))
            {
                return;
            }
            if (lexeme.isToken(Token.IDENTIFIER))
            {
                String key=lexeme.getValue();
                lexeme = next();
                if (lexeme.isToken(Token.ASSIGNMENT) == false)
                {
                    throw new Exception();
                }
                lexeme= next();
                if (lexeme.isToken(Token.STRING) == false)
                {
                    throw new Exception();
                }
                String value=lexeme.getValue();
                if (key.equals("var"))
                {
                	this.parseContext.addSection(value);
                }
                else if (key.equals("file"))
                {
                	Parser parser=new Parser(parseContext);
                	parser.parse(value);
                }
                else if (key.equals("varFile"))
                {
                	String file=this.parseContext.parameters.get(value);
                	Parser parser=new Parser(parseContext);
                	parser.parse(file);
                }
            } 
            else
            {
                throw new Exception();
            }
        }

    }
    
    
    void parseBeginTag(Lexeme beginTag) throws Throwable
    {
        Lexeme lexeme = next();
        if (lexeme.isToken(Token.SLASH))
        {
            this.parseContext.sectionText.append('<');
            this.parseContext.sectionText.append(lexeme.getValue());
            lexeme = next();
            if (lexeme.isToken(Token.IDENTIFIER))
            {
                this.parseContext.sectionText.append(lexeme.getValue());
                lexeme = next();
            }
            if (lexeme.isToken(Token.END_TAG) == false)
            {
                throw new Exception();
            }
            this.parseContext.sectionText.append('>');
            return;
        }
        if (lexeme.isToken(Token.IDENTIFIER)==false)
        {
            throw new Exception();
        }
        if (lexeme.getValue().equals("insert"))
        {
            process();
            return;
        }
        this.parseContext.sectionText.append('<');
        this.parseContext.sectionText.append(lexeme.getValue());
        for (;;)
        {
            lexeme = next();
            if (lexeme.isToken(Token.SLASH))
            {
                this.parseContext.sectionText.append('/');
                lexeme = next();
                if (lexeme.isToken(Token.END_TAG) == false)
                {
                    throw new Exception();
                }
                this.parseContext.sectionText.append('>');
                return;
                
            }
            if (lexeme.isToken(Token.END_TAG))
            {
                this.parseContext.sectionText.append(lexeme.getValue());
                return;
            }
            if (lexeme.isToken(Token.IDENTIFIER))
            {
                this.parseContext.sectionText.append(' ');
                this.parseContext.sectionText.append(lexeme.getValue());
                lexeme = next();
                if (lexeme.isToken(Token.ASSIGNMENT) == false)
                {
                    throw new Exception();
                }
                this.parseContext.sectionText.append(lexeme.getValue());
                lexeme = next();
                if (lexeme.isToken(Token.STRING) == false)
                {
                    throw new Exception();
                }
                this.parseContext.sectionText.append(lexeme.getLiteral());
            } 
            else
            {
                throw new Exception();
            }
        }

    }

    void parse() throws Throwable
    {
        Lexeme lexeme = next();
        if (lexeme.isToken(Token.DOCTYPE))
        {
            this.parseContext.sectionText.append(lexeme.getLiteral());
            lexeme = next();
        }
        for (; lexeme.isToken(Token.END) == false; lexeme = next())
        {
            switch (lexeme.getToken())
            {
            case TEXT:
                this.parseContext.sectionText.append(lexeme.getValue());
                break;

            case BEGIN_TAG:
                parseBeginTag(lexeme);
                break;

            case ERROR:
                throw new Exception();
            case ASSIGNMENT:
            case IDENTIFIER:
            case OPERATOR:
            case STRING:
                throw new Exception();
            default:
                break;

            }
        }
    }

}
