package org.nova.operations;

public class ValidationResult
{
    final Object result;
    final Status status;
    final String message;
    public ValidationResult(Status status,Object result,String message)
    {
        this.status=status;
        this.result=result;
        this.message=message;
    }
    public ValidationResult()
    {
        this(Status.SUCCESS,null,null);
    }
    public ValidationResult(Object result)
    {
        this(Status.SUCCESS,result,null);
    }
    public ValidationResult(String message)
    {
        this(Status.VALIDATION_FAILED,null,message);
    }
    public Object getResult()
    {
        return result;
    }
    public Status getStatus()
    {
        return status;
    }
    public String getMessage()
    {
        return this.message;
    }
}
