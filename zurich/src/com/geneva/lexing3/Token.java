package com.geneva.lexing3;

public enum Token
{
    KEYWORD,
    OPERATOR,
    PUNCTUATOR,
    ASSIGNMENT,
//    COMMA,
    TERMINATOR,
    SEPERATOR,
    
    OPEN_ROUND,
    CLOSE_ROUND,
    OPEN_SQUARE,
    CLOSE_SQUARE,
    OPEN_CURLY,
    CLOSE_CURLY,
    SEQUENCE,
    WORD,
    STRING,
    IDENTIFIER,
    CHARACTER,
    NUMBER,
	//specials
    ERROR,
    END,
}
