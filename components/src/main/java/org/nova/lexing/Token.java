package org.nova.lexing;

public enum Token
{
    OPERATOR,
    PUNCTUATOR,
    TERMINATOR,
    SEPERATOR,
    TEXT, //Any text that is not string, eg, keywords, delimited text, etc  
    STRING, 
    CHARACTER,
    NUMBER,
	//specials
    ERROR,
//    END,
}
