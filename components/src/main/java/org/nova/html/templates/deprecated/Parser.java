package org.nova.html.templates.deprecated;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.html.templates.deprecated.InsertSection;
import org.nova.html.templates.deprecated.Section;
import org.nova.html.templates.deprecated.StaticSection;
import org.nova.parsing.scan.Lexeme;
import org.nova.parsing.scan.Scanner;
import org.nova.parsing.scan.TextSource;
import org.nova.parsing.scan.Token;
import org.nova.utils.FileUtils;

public class Parser
{
	private StringBuilder sb;
	final private ArrayList<Section> sections;
    private Scanner lexer;

    public Parser()
    {
        this.sb=new StringBuilder();
        this.sections=new ArrayList<>();
    }
    
    Section[] parseFile(String htmlFile) throws Throwable
    {
        return parseText(FileUtils.readTextFile(htmlFile, StandardCharsets.UTF_8));
    }

    Section[] parseText(String text) throws Throwable
    {
        TextSource source=new TextSource(text);
        this.lexer = new Scanner(source);
        parse();
        if (this.sb.length()>0)
        {
            this.sections.add(new StaticSection(this.sb.toString()));
        }
        return this.sections.toArray(new Section[this.sections.size()]);
    }
    
    private void parse() throws Throwable
    {
        for (;;)
        {
            char c = this.lexer.skipWhiteSpaceAndBegin();
            if (c==0)
            {
                return;
            }
            if (c=='<')
            {
                c=this.lexer.read();
                if (Character.isLetter(c))
                {
                    parseTag();
                }
                else if (c=='!')
                {
                    Lexeme lexeme=this.lexer.produceDelimitedText('>',true);
                    this.sb.append(lexeme.getSnippet().getTarget());
                }
                else if (c=='/')
                {
                    Lexeme lexeme=this.lexer.produceDelimitedText('>',true);
                    this.sb.append(lexeme.getSnippet().getTarget());
                }
                else
                {
                    Lexeme lexeme=this.lexer.produceToken(Token.TEXT, 0);
                    this.sb.append(lexeme.getSnippet().getTarget());
                }
            }
            else
            {
                parseText();
            }
        }
    }

    private void parseText() throws Throwable
    {
        for (;;)
        {
            char c=this.lexer.read();
            if (c==' ')
            {
                Lexeme lexeme=this.lexer.produceToken(Token.TEXT, 0);
                this.sb.append(lexeme.getSnippet().getTarget());
                break;
            }
            else if ((c=='<')||(c==0))
            {
                Lexeme lexeme=this.lexer.produceToken(Token.TEXT, 1);
                this.sb.append(lexeme.getSnippet().getTarget());
                break;
            }
        }
        
    }

    private void parseInvalidText() throws Throwable
    {
        for (;;)
        {
            char c=this.lexer.read();
            if ((c=='/')||(c==' ')||(c=='>'))
            {
                Lexeme lexeme=this.lexer.produceToken(Token.TEXT, 1);
                this.sb.append(lexeme.getSnippet().getTarget());
                return;
            }
        }
    }
    
    private void parseInsert() throws Throwable
    {
        char c=lexer.skipWhiteSpaceAndBegin();
        Lexeme lexeme = lexer.produceWord();
        String name=lexeme.getValue();
        c=lexer.skipWhiteSpaceAndBegin();
        if (c!='=')
        {
            lexer.revert();
            return;
        }
        this.lexer.end(0);
        c=lexer.skipWhiteSpaceAndBegin();
        if ((c=='"')||(c=='\"'))
        {
            lexeme=this.lexer.produceSimpleString(c);
            if ("key".equals(name))
            {
                this.sections.add(new StaticSection(this.sb.toString()));
                this.sb=new StringBuilder();
                this.sections.add(new InsertSection(lexeme.getValue()));

                c=this.lexer.skipWhiteSpaceAndBegin();
                if (c=='>')
                {
                    this.lexer.end(0);
                    return;
                }
                else if (c=='/')
                {
                    c=this.lexer.skipWhiteSpaceAndBegin();
                    if (c=='>')
                    {
                        this.lexer.end(0);
                        return;
                    }
                }
            }
        }
        parseInvalidText();
    }
    
    
    private void parseTag() throws Throwable
    {
        Lexeme lexeme = lexer.produceWord();
        if ("<insert".equals(lexeme.getValue()))
        {
            parseInsert();
            return;
        }
        this.sb.append(lexeme.getSnippet().getTarget());
        for (;;)
        {
            char c=this.lexer.skipWhiteSpaceAndBegin();
            if (c=='>')
            {
                this.lexer.end(0);
                this.sb.append(">");
                return;
            }
            else if (c=='/')
            {
                c=this.lexer.read();
                if (c=='>')
                {
                    this.lexer.end(0);
                    this.sb.append(" />");
                    return;
                }
                parseInvalidText();
            }
            else
            {
                this.sb.append(' ');
                parseAttribute();
            }
        }
    }
     
    private void parseAttribute() throws Throwable
    {
        Lexeme lexeme = lexer.produceWord();
        this.sb.append(lexeme.getSnippet().getTarget());
        char c=lexer.skipWhiteSpaceAndBegin();
        if (c!='=')
        {
            lexer.revert();
            return;
        }
        this.lexer.end(0);
        this.sb.append(c);
        c=lexer.skipWhiteSpaceAndBegin();
        if ((c=='"')||(c=='\"'))
        {
            lexeme=this.lexer.produceSimpleString(c);
            this.sb.append(lexeme.getSnippet().getTarget());
            return;
        }
        parseInvalidText();
    }

}
