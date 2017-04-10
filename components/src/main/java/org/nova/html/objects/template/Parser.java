package org.nova.html.objects.template;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.nova.collections.FileCache;
import org.nova.core.Utils;
import org.nova.lexing.Lexer;
import org.nova.lexing.TextSource;

import com.geneva.util.Source;

/*
 * <server file="file"|includeKey="key">
 * 
 */

public class Parser
{
    Lexer lexer;

    public void parse(String html) throws Throwable
    {
        TextSource source=new TextSource(html);
        this.lexer = new Lexer(source);
        parseDOCTYPE();
        parseTags();
    }

    void parse() throws Throwable
    {
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


}
