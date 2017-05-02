package org.nova.validation;

public class ValidationViolation
{
    final private Violation violation;
    final private String message;
    public ValidationViolation(Violation violation,String message)
    {
        this.violation=violation;
        this.message=message;
    }
    public ValidationViolation(Violation violation)
    {
        this(violation,null);
    }
    public Violation getViolation()
    {
        return violation;
    }
    public String getMessage()
    {
        return message;
    }
    
}
