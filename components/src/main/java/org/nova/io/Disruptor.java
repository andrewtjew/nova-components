package org.nova.io;

import java.lang.reflect.Field;
import java.util.Random;

import org.nova.operations.OperatorVariable;
import org.nova.operations.Status;
import org.nova.operations.ValidationResult;
import org.nova.operations.Validator;
import org.nova.operations.VariableInstance;

/* This class is to simulate network or other IO failures.
 * 
 */
public class Disruptor implements Validator
{
    @OperatorVariable(validator=Disruptor.class)
    private boolean latency;
    
    @OperatorVariable(validator=Disruptor.class)
    private long minLatency;
    
    @OperatorVariable(validator=Disruptor.class,options={"100","200","300"})
    private long maxLatency;

    private long deterministicExceptionCountdown; //set to zero to disable
    private long probabilityExceptionThreshold;
    
    private long deterministicLatencyCountdown; //set to zero to disable
    private long probabilityLatencyThreshold;
    
    final Random random;
    long counter;

    public Disruptor(long minLatency,long maxLatency,long deterministicExceptionCountdown,double exceptionProbability,long seed)
    {
        this.random=new Random(seed);
        
    }

    private ValidationResult validate(Field field,Object value)
    {
        if (field.getName()=="minLatency")
        {
            if ((long)value>this.maxLatency)
            {
                return new ValidationResult("minLatency cannot be greater than maxLatency"); 
            }
        }
        return new ValidationResult(value);
    }
    
    @Override
    public ValidationResult validate(VariableInstance instance, Object value) throws Throwable
    {
        return ((Disruptor)instance.getObject()).validate(instance.getField(), value);
    }
}
