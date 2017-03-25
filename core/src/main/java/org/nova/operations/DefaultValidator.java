package org.nova.operations;

public class DefaultValidator implements Validator
{
    @Override
    public ValidationResult validate(VariableInstance instance, Object value) throws Throwable
    {
        return new ValidationResult(value);
    }
}
