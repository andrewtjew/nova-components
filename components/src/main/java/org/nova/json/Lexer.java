package org.nova.json;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Lexer
{
	abstract static class Reader
	{
		abstract char read() throws Exception;
//		abstract String readJsonName() throws Exception;
		abstract int getPosition();
	}
	
	static class StreamReader extends Reader
	{
		final private BufferedReader reader;
		private int position;
		StreamReader(InputStream inputStream) throws Exception
		{
			this.reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
		}
		@Override
		public char read() throws Exception
		{
			this.position++;
			return (char)this.reader.read();
		}
		@Override
		int getPosition()
		{
			return this.position;
		}
		/*
		@Override
		String readJsonName() throws Exception
		{
			StringBuilder sb=new StringBuilder();
			for (;;)
			{
				char c=read();
				if (c=='"')
				{
					for (;;)
					{
						c=read();
						if (c==':')
						{
							return sb.toString();
						}						
					}
				}
				sb.append(c);
			}
		}
		*/
	}

	static class StringReader extends Reader
	{
		final private String text;
		private int position;
		final private int length;
		StringReader(String text) throws Exception
		{
			this.text=text;
			this.length=text.length();
		}
		@Override
		public char read() throws Exception
		{
			return text.charAt(this.position++);
		}
		@Override
		int getPosition()
		{
			return this.position;
		}
		/*
		@Override
		String readJsonName() throws Exception
		{
			int index=position;
			for (;;)
			{
				char c=this.text.charAt(index++);
				if (c=='"')
				{
					String name=this.text.substring(position, index-1);
					for (;;)
					{
						c=this.text.charAt(index++);
						if (c==':')
						{
							this.position=index;
							return name;
						}						
					}
				}
						
			}
		}
		*/
	}
	
	final Reader reader;
	public Lexer(InputStream inputStream) throws Exception
	{
		this.reader=new StreamReader(inputStream);
	}
	public Lexer(String text) throws Exception
	{
		this.reader=new StringReader(text);
	}
	
/*	
	 %x20 /              ; Space
     %x09 /              ; Horizontal tab
     %x0A /              ; Line feed or New line
     %x0D                ; Carriage return
*/
	
	private char skipWhiteSpace() throws Exception
	{
		char character=this.last>0?this.last:reader.read();
		for (;;)
		{
			if (character>0x20)
			{
				this.last=character;
				return character;
			}
			if ((character!=0x20)&&(character!=0x09)&&(character!=0x0A)&&(character!=0x0D))
			{
				this.last=character;
				return character;
			}
			character=reader.read();
		}
	}
	
	public int getPosition()
	{
		return this.reader.getPosition();
	}
	private char last=0;
	public char getNextCharacter() throws Exception
	{
		char c=skipWhiteSpace();
		this.last=0;
		return c;
	}
	
	public String getJSONName() throws Exception
	{
        StringBuilder sb=new StringBuilder();
        for (;;)
        {
            char c=this.reader.read();
            if (c=='"')
            {
                for (;;)
                {
                    c=this.reader.read();
                    if (c==':')
                    {
                        return sb.toString();
                    }                       
                }
            }
            sb.append(c);
        }
	}
	
	public String getString() throws Exception
	{
	    
		char character=skipWhiteSpace();
        if (character=='"')
        {
            StringBuilder sb=new StringBuilder();
    		for (;;)
    		{
    			character=this.reader.read();
    			if (character=='"')
    			{
                    this.last=0;
    				return sb.toString();
    			}
    			else if (character=='\\')
    			{
    				int next=this.reader.read();
    				if (next=='"')
    				{
    					sb.append('"');
    				}
    				else if (next=='\\')
    				{
    					sb.append('\\');
    				}
    				else if (next=='/')
    				{
    					sb.append('/');
    				}
    				else if (next=='b')
    				{
    					sb.append('\b');
    				}
    				else if (next=='f')
    				{
    					sb.append('\f');
    				}
    				else if (next=='n')
    				{
    					sb.append('\n');
    				}
    				else if (next=='r')
    				{
    					sb.append('\r');
    				}
    				else if (next=='t')
    				{
    					sb.append('\t');
    				}
    				else if (next=='u')
    				{
    					char c=0;
    					for (int i=0;i<4;i++)
    					{
    						int digit=this.reader.read();
    						if ((digit>='0')&&(digit<='9'))
    						{
    							c=(char)(c*16+digit-'0');
    						}
    						else if ((digit>='A')&&(digit<='E'))
    						{
    							c=(char)(c*16+digit-'A'+10);
    						}
    						else if ((digit>='a')&&(digit<='e'))
    						{
    							c=(char)(c*16+digit-'a'+10);
    						}
    						else
    						{
    							throw new Exception("Invalid unicode escape at "+getPosition());
    						}
    					}
    					sb.append(c);
    				}
    			}
    			else
    			{
    				sb.append(character);
    			}
    		}
        }
        else if  (character=='n')
        {
            if (this.reader.read()=='u')
            {
                if (this.reader.read()=='l')
                {
                    if (this.reader.read()=='l')
                    {
                        this.last=0;
                        return null;
                    }
                }
            }
        }
        throw new Exception("String or null expected at "+getPosition());
	}

	public boolean getRestOfNull() throws Exception
    {
        if (this.reader.read()=='u')
        {
            if (this.reader.read()=='l')
            {
                if (this.reader.read()=='l')
                {
                    this.last=0;
                    return true;
                }
            }
        }
        throw new Exception("null expected at "+getPosition());

    }

	public Boolean getBoolean() throws Exception
	{
		char character=skipWhiteSpace();
		if (character=='t')
		{
			if (this.reader.read()=='r')
			{
				if (this.reader.read()=='u')
				{
					if (this.reader.read()=='e')
					{
                        this.last=0;
						return true;
					}
				}
			}
		}
		else if (character=='f')
		{
			if (this.reader.read()=='a')
			{
				if (this.reader.read()=='l')
				{
					if (this.reader.read()=='s')
					{
						if (this.reader.read()=='e')
						{
	                        this.last=0;
							return false;
						}
					}
				}
			}
		}
		else if (character=='n')
		{
			if (this.reader.read()=='u')
			{
				if (this.reader.read()=='l')
				{
					if (this.reader.read()=='l')
					{
                        this.last=0;
						return null;
					}
				}
			}
		}
		throw new Exception("Boolean value or null expected at "+getPosition());
	}
	
	public void skipJSONValue() throws Exception
	{
	    char c=this.skipWhiteSpace();
	    if (c=='"')
	    {
	        getString(); //we can optimize this by noticing that we don't need the computed string.
	    }
	    else
	    {
	        char character=skipWhiteSpace();
	        for (;;)
	        {
	            if ((character==',')||(character=='}')||(character==']'))
	            {
	                this.last=character;
	                break;
	            }
	            if (character<=0x20)
	            {
	                if ((character==0x20)||(character==0x09)||(character==0x0A)||(character==0x0D))
	                {
	                    this.last=0;
	                    skipWhiteSpace();
	                    break;
	                }
	            }
	            character=this.reader.read();
	        }
	    }
	}
	
	
	public boolean getPrimitiveBoolean() throws Exception
	{
		char character=skipWhiteSpace();
		if (character=='t')
		{
			if (this.reader.read()=='r')
			{
				if (this.reader.read()=='u')
				{
					if (this.reader.read()=='e')
					{
	                    this.last=0;
						return true;
					}
				}
			}
		}
		else if (character=='f')
		{
			if (this.reader.read()=='a')
			{
				if (this.reader.read()=='l')
				{
					if (this.reader.read()=='s')
					{
						if (this.reader.read()=='e')
						{
		                    this.last=0;
							return false;
						}
					}
				}
			}
		}
		throw new Exception("Boolean value expected at "+getPosition());
	}

	String getValueText() throws Exception
	{
		StringBuilder sb=new StringBuilder();
		char character=skipWhiteSpace();
		for (;;)
		{
			if ((character==',')||(character=='}')||(character==']'))
			{
				this.last=character;
				break;
			}
			if (character<=0x20)
			{
				if ((character==0x20)||(character==0x09)||(character==0x0A)||(character==0x0D))
				{
					this.last=0;
					skipWhiteSpace();
					break;
				}
			}
			sb.append(character);
			character=this.reader.read();
		}
		return sb.toString();
	}

	char isCommaOrCloseArray() throws Exception
	{
		char character=skipWhiteSpace();
		if ((character==',')||(character==']'))
		{
			this.last=0;
			return character;
		}
		this.last=character;
		return 0;
	}
	char isCloseArray() throws Exception
	{
		char character=skipWhiteSpace();
		if (character==']')
		{
			this.last=0;
			return character;
		}
		this.last=character;
		return 0;
	}
	
	public byte getPrimitiveByte() throws Exception
	{
		try
		{
			return Byte.parseByte(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("byte value expected at "+getPosition());
		}
	}
	public char getPrimitiveCharacter() throws Exception
	{
		try
		{
			return (char)Integer.parseUnsignedInt(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("char value expected at "+getPosition());
		}
	}
	public short getPrimitiveShort() throws Exception
	{
		try
		{
			return Short.parseShort(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("short value expected at "+getPosition());
		}
	}
	public int getPrimitiveInteger() throws Exception
	{
		String text=getValueText();
		try
		{
			return Integer.parseInt(text);
		}
		catch (Throwable t)
		{
			throw new Exception("int value expected at "+getPosition()+", text="+text);
		}
	}
	public long getPrimitiveLong() throws Exception
	{
		try
		{
			return Long.parseLong(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("long value expected at "+getPosition());
		}
	}
	public float getPrimitiveFloat() throws Exception
	{
		try
		{
			return Float.parseFloat(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("float value expected at "+getPosition());
		}
	}
	public double getPrimitiveDouble() throws Exception
	{
		try
		{
			return Double.parseDouble(getValueText());
		}
		catch (Throwable t)
		{
			throw new Exception("double value expected at "+getPosition());
		}
	}
	
	//-------------
	public Byte getByte() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Byte.parseByte(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Byte value expected at "+getPosition());
		}
	}
	public Character getCharacter() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return (char)Integer.parseUnsignedInt(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Character value expected at "+getPosition());
		}
	}
	public Short getShort() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Short.parseShort(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Short value expected at "+getPosition());
		}
	}
	public Integer getInteger() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Integer.parseInt(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Integer value expected at "+getPosition());
		}
	}
	public Long getLong() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Long.parseLong(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Long value expected at "+getPosition());
		}
	}
	public Float getFloat() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Float.parseFloat(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Float value expected at "+getPosition());
		}
	}
	public Double getDouble() throws Exception
	{
		try
		{
			String text=getValueText();
			if ("null".equals(text))
			{
				return null;
			}
			return Double.parseDouble(text);
		}
		catch (Throwable t)
		{
			throw new Exception("Double value expected at "+getPosition());
		}
	}

}


