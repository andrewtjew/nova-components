package org.nova.validation;

public abstract class Validator<TYPE>
{
    public abstract ValidationViolation validate(TYPE value) throws Throwable;

}
