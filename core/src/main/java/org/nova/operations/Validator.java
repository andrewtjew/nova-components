package org.nova.operations;

public interface Validator
{
    public ValidationResult validate(VariableInstance instance,Object value) throws Throwable;
}
