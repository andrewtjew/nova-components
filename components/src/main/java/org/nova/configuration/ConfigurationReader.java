package org.nova.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.nova.core.Utils;
import org.nova.lexing.Lexeme;
import org.nova.lexing.Lexer;
import org.nova.lexing.LineAndColumn;
import org.nova.lexing.TextSource;

public class ConfigurationReader
{
	final Configuration configuration;
	final FileReader reader;
	final String fileName;

	private ConfigurationReader(String fileName, Configuration configuration) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.reader = new FileReader(fileName);
		this.configuration = configuration;
	}

	private void checkError(Lexeme lexeme) throws Exception
	{
	    if (lexeme.isError())
	    {
            LineAndColumn lc=new LineAndColumn(lexeme.getSnippet().getContext(), lexeme.getSnippet().getAbsolutePosition());
            throw new Exception("Error at " + this.fileName + "(" + (lc.getLine()+1)+":"+(lc.getColumn()+1)+")");
	    }
	}
	private void read() throws Throwable
	{
	    String text=Utils.readTextFile(this.fileName);
	    TextSource source=new TextSource(text);
	    Lexer lexer=new Lexer(source);
	    lexer.skipWhiteSpace();
	    String description=null;
		for (;;)
		{
		    char character=lexer.skipWhiteSpaceAndBegin();
			if (character==0)
			{
				return;
			}
			if (character=='#')
			{
			    Lexeme lexeme=lexer.produceTerminatedText('\r','\n');
			    checkError(lexeme);
			    description=lexeme.getValue().substring(1);
			}
			else if (character=='/')
            {
                Lexeme lexeme=lexer.produceSlashSlashComment();
                checkError(lexeme);
            }
			else if (character=='@')
            {
                Lexeme lexeme=lexer.produceTerminatedText('\r','\n');
                String file=lexeme.getValue().substring(1).trim();
                int index=this.fileName.lastIndexOf('/');
                if (index<0)
                {
                    index=this.fileName.lastIndexOf('\\');
                }
                if (index>=0)
                {
                    file=this.fileName.substring(0, index+1)+file;
                }
                read(file,this.configuration);
            }
			else
			{
    			Lexeme lexeme=lexer.produceTerminatedText('=');
                checkError(lexeme);
    			String name=lexeme.getValue().trim();
    
    			lexeme=lexer.expectPunctuator('=');
                checkError(lexeme);
    
                String value=null;
                character=lexer.skipWhiteSpaceAndBegin();
                if (character == '{')
                {
                    lexeme=lexer.produceEnclosedJSONText('{', '}');
                }
                else if (character == '[')
                {
                    lexeme=lexer.produceEnclosedJSONText('[', ']');
                }
                else
                {
                    lexeme=lexer.produceTerminatedText('\r','\n');
                }
                int line=new LineAndColumn(text,lexeme.getSnippet().getAbsolutePosition()).getLine()+1;
    			this.configuration.add(new ConfigurationItem(name, lexeme.getValue().trim(), ConfigurationSource.FILE,fileName+"("+line+")", description));
    			description=null;
			}
		}

	}

	public static void read(String fileName,Configuration configuration) throws Throwable
	{
		ConfigurationReader reader=new ConfigurationReader(fileName, configuration);
		reader.read();
	}
	
    public static Configuration read(String fileName) throws Throwable
	{
		Configuration configuration=new Configuration();
		ConfigurationReader reader=new ConfigurationReader(fileName, configuration);
		reader.read();
		return configuration;
	}

	public static Configuration tryRead(String fileName) throws Exception
	{
		Configuration configuration=new Configuration();
		try
		{
			ConfigurationReader reader=new ConfigurationReader(fileName, configuration);
			reader.read();
		}
		catch (Throwable t)
		{
			configuration.add(new ConfigurationItem("error.configuration.tryRead", fileName, ConfigurationSource.ERROR, Utils.getStrackTraceAsString(t), "Unable to read configuration file. Check if file exists and is readable."));
		}
		return configuration;
	}
	
    public static Configuration search(String[] args)
    {
        ArrayList<String> candidates=new ArrayList<>();
        for (String arg:args)
        {
            String[] parts=Utils.split(arg, '=');
            if ((parts.length==2)&&("config".equals(parts[0])))
            {
                candidates.add(parts[1]);
            }
        }
        candidates.add("./resources/dev.cnf");
        candidates.add("./resources/integration.cnf");
        candidates.add("./resources/test.cnf");
        candidates.add("./resources/stage.cnf");
        candidates.add("./resources/production.cnf");

        return search(args,candidates.toArray(new String[candidates.size()]));
    }
	public static Configuration search(String[] args,String[] candidates)
	{
        for (int i=0;i<candidates.length;i++)
        {
            //So we are OK with Windows or Linux
            candidates[i]=candidates[i].replace("/", File.separator).replace("\\", File.separator);
        }

        for (String candidate:candidates)
        {
            try
            {
                File file=new File(candidate);
                String path=file.getCanonicalPath();
                System.out.print("Trying to use "+path+"...");
                if (file.exists()==false)
                {
                    System.out.println("does not exist");
                    continue;
                }
                Configuration configuration=read(path);
                configuration.addCommandLineConfigurations(args);
                System.out.println("OK");
                return configuration;
            }
            catch (Throwable t)
            {
               t.printStackTrace(System.err);
            }
        }
        System.out.print("No suitable configuration file found");
        return null;
    }
}
