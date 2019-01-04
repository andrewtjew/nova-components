package org.nova.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.nova.parsing.scan.Lexeme;
import org.nova.parsing.scan.LineAndColumn;
import org.nova.parsing.scan.Scanner;
import org.nova.parsing.scan.TextSource;
import org.nova.utils.FileUtils;

public class ConfigurationReader
{
	final Configuration configuration;
	final FileReader reader;
	final String fileName;

	private ConfigurationReader(String fileName, Configuration configuration) throws FileNotFoundException
	{
		this.fileName = fileName;
		this.reader = new FileReader(FileUtils.toNativePath(fileName));
		this.configuration = configuration;
	}

	private void checkError(Lexeme lexeme) throws Exception
	{
	    if (lexeme.isError())
	    {
            LineAndColumn lc=new LineAndColumn(lexeme.getSnippet().getBuffer(), lexeme.getSnippet().getTargetAbsolutePosition());
            throw new Exception("Error at " + this.fileName + "(" + (lc.getLine()+1)+":"+(lc.getColumn()+1)+")");
	    }
	}
	private void read() throws Throwable
	{
	    String text=FileUtils.readTextFile(this.fileName);
	    TextSource source=new TextSource(text);
	    Scanner scanner=new Scanner(source);
	    scanner.skipWhiteSpace();
	    String description=null;
		for (;;)
		{
		    char character=scanner.skipWhiteSpaceAndBegin();
			if (character==0)
			{
				return;
			}
			else if (character=='#') //comment 
			{
			    Lexeme lexeme=scanner.produceTerminatedText('\r','\n');
			    checkError(lexeme);
			}
			else if (character=='!') //description 
            {
                Lexeme lexeme=scanner.produceTerminatedText('\r','\n');
                checkError(lexeme);
                description=lexeme.getValue().substring(1);
            }
			else if (character==';') //optional terminator
            {
                continue;
            }
			else if (character=='/') //comment
            {
                Lexeme lexeme=scanner.produceSlashSlashComment();
                checkError(lexeme);
            }
            else if (character=='@') //include
            {
                Lexeme lexeme=scanner.produceTerminatedText('\r','\n');
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
            else if (character=='%') //optional include
            {
                Lexeme lexeme=scanner.produceTerminatedText('\r','\n');
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
                try
                {
                    read(file,this.configuration);
                }
                catch (Throwable t)
                {
                }
            }
			else
			{
    			Lexeme lexeme=scanner.produceTerminatedText('=');
                checkError(lexeme);
    			String name=lexeme.getValue().trim();
    
    			lexeme=scanner.expectPunctuator('=');
                checkError(lexeme);
    
                character=scanner.skipWhiteSpaceAndBegin();
                if (character == '{')
                {
                    lexeme=scanner.produceEnclosedJSONText('{', '}');
                }
                else if (character == '[')
                {
                    lexeme=scanner.produceEnclosedJSONText('[', ']');
                }
                else
                {
                    lexeme=scanner.produceTerminatedTextAndSkipTerminator('\r','\n',';');
                }
                int line=new LineAndColumn(text,lexeme.getSnippet().getTargetAbsolutePosition()).getLine()+1;
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
	
    public static Configuration read(String fileName) 
	{
        try
        {
            Configuration configuration=new Configuration();
    		ConfigurationReader reader=new ConfigurationReader(fileName, configuration);
    		reader.read();
    		return configuration;
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
            return null;
        }
	}

    public static Configuration read(String args[],String configurationFileKey)
    {
        try
        {
            if (configurationFileKey==null)
            {
                configurationFileKey="config";
            }
            Configuration configuration=new Configuration();
            configuration.addArgs(args);
            String fileName=configuration.getValue(configurationFileKey);
            read(fileName,configuration);
            configuration.addArgs(args);
            return configuration;
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
            return null;
        }
    }
	
//    public static Configuration search(String[] args)
//    {
//        ArrayList<String> candidates=new ArrayList<>();
//        for (String arg:args)
//        {
//            String[] parts=Utils.split(arg, '=');
//            if ((parts.length==2)&&("config".equals(parts[0])))
//            {
//                candidates.add(parts[1]);
//            }
//        }
//        candidates.add("./resources/dev.cnf");
//        candidates.add("./resources/integration.cnf");
//        candidates.add("./resources/test.cnf");
//        candidates.add("./resources/stage.cnf");
//        candidates.add("./resources/production.cnf");
//
//        return search(args,candidates.toArray(new String[candidates.size()]));
//    }
//    
//    
//	private static Configuration search(String[] args,String[] candidates)
//	{
//        for (int i=0;i<candidates.length;i++)
//        {
//            //So we are OK with Windows or Linux
//            candidates[i]=candidates[i].replace("/", File.separator).replace("\\", File.separator);
//        }
//
//        for (String candidate:candidates)
//        {
//            try
//            {
//                File file=new File(candidate);
//                String path=file.getCanonicalPath();
//                System.out.print("Trying to use "+path+"...");
//                if (file.exists()==false)
//                {
//                    System.out.println("does not exist");
//                    continue;
//                }
//                Configuration configuration=read(path);
//                configuration.addArgs(args);
//                System.out.println("OK");
//                return configuration;
//            }
//            catch (Throwable t)
//            {
//               t.printStackTrace(System.err);
//            }
//        }
//        System.out.print("No suitable configuration file found");
//        return null;
//    }
}
