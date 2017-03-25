package org.nova.lexing;

public enum Token
{
    OPERATOR,
    PUNCTUATOR,
    TERMINATOR,
    SEPERATOR,
    
//    OPEN_ROUND,
//    CLOSE_ROUND,

    TEXT, //A word is any text between white space, terminatorss and seperators
    STRING,
    CHARACTER,
    NUMBER,
	//specials
    ERROR,
    END,
}
