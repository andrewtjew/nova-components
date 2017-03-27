package com.geneva.util;

public class Source
{
    private final String text;
    private final String fileName;
    
    public Source(String text,String fileName)
    {
	this.text=text;
	this.fileName=fileName;
    }

    public String getText()
    {
        return text;
    }

    public String getFileName()
    {
        return fileName;
    }
    
    
}
