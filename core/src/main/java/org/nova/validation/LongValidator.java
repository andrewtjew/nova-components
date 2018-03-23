package org.nova.validation;

import java.util.HashSet;

public class LongValidator extends Validator<Long>
{
    private Long max;
    private Long min;
    private boolean required;
    private HashSet<Long> options;

    public LongValidator(Long min,Long max,boolean required,long[] options)
    {
        this.max=max;
        this.min=min;
        this.required=required;
        if (options!=null)
        {
            this.options=new HashSet<>();
            for (long option:options)
            {
                this.options.add(option);
            }
        }
    }
    
    @Override
    public ValidationViolation validate(Long value) throws Throwable
    {
        if (value!=null)
        {
            if ((this.min!=null)&&(value<this.min))
            {
                return new ValidationViolation(Violation.MIN);
            }
            if ((this.max!=null)&&(value<this.max))
            {
                return new ValidationViolation(Violation.MAX);
            }
            if (this.options!=null)
            {
                if (this.options.contains(value)==false)
                {
                    return new ValidationViolation(Violation.OPTIONS);
                }
            }
        }
        else if (this.required)
        {
            return new ValidationViolation(Violation.REQUIRED);
        }
        return null;
        
    }

    
}
