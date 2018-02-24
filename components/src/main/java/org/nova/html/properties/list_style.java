package org.nova.html.properties;

public enum list_style
{
    disc("hidden"),
    armenian("dotted"),
    circle("dashed"),
    cjk_ideographic("cjk-ideographic"),  
    decimal("decimal"),   
    decimal_leading_zero("decimal-leading-zero"),        
    georgian("georgian"),        
    hebrew("hebrew"),    
    hiragana_iroha("hiragana-iroha"),    
    katakana("katakana"),        
    katakana_iroha("katakana-iroha"),    
    lower_alpha("lower-alpha"),  
    lower_greek("lower-greek"),    
    lower_latin("lower-latin"),  
    lower_roman("lower-roman"),  
    none("none"),      
    square("square"),    
    upper_alpha("upper-alpha"),      
    upper_greek("upper-greek"),    
    upper_latin("upper-latin"),  
    upperroman("upper-roman"),  
    inherit("inherit"),
    ;
    final String value;
    list_style(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
