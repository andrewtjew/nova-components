package org.nova.html;

import java.util.ArrayList;

import org.nova.core.Pair;
import org.nova.parsing.scan.Lexeme;
import org.nova.parsing.scan.Scanner;
import org.nova.parsing.scan.TextSource;

public class Accept
{
    static class QValue extends Pair<Float,String>
    {
        public QValue(Float key, String value)
        {
            super(key, value);
        }
        
    }
    public Accept(String value) throws Throwable
    {
        ArrayList<QValue> list=new ArrayList<>();
        Scanner scanner=new Scanner(new TextSource(value));
        for (;;)
        {
            char c=scanner.skipWhiteSpaceAndBegin();
            if (c==0)
            {
                break;
            }
            Lexeme lexeme=scanner.produceTerminatedText(',',';');
            String type=lexeme.getValue();
            c=scanner.skipWhiteSpaceAndBegin();
            float q=1;
            if (c==';')
            {
                c=scanner.skipWhiteSpaceAndBegin();
                if (c!='q')
                {
                    throw new Exception("q expected");
                }
                c=scanner.skipWhiteSpaceAndBegin();
                if (c!='=')
                {
                    throw new Exception("= expected");
                }
                lexeme=scanner.produceNumber(true);
            }
                
            
        }
            
    }
}
