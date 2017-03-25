package org.nova.operations;

import java.lang.reflect.Field;

public interface Validator
{
    public ValidationResult validate(VariableInstance instance,Object value) throws Throwable;
}
