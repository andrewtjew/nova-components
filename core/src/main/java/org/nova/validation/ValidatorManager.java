package org.nova.validation;

import java.util.HashMap;

public class ValidatorManager
{
    HashMap<String,Validator<?>> validators;
    
    
    public <TYPE> Validator<TYPE> getValidator(String key)
    {
        return (Validator<TYPE>) this.validators.get(key);
    }
}
