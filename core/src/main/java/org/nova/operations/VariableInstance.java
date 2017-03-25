package org.nova.operations;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VariableInstance
{
    private final Validator validator;
	private OperatorVariable variable;
	private final Object object;
	private final Field field;
	private long modified;
	private final Object defaultValue;
	VariableInstance(Validator validator,OperatorVariable variable,Object object,Field field) throws Throwable
	{
	    this.validator=validator;
		field.setAccessible(true);
		this.defaultValue=field.get(object);
		this.variable=variable;
		this.field=field;
		this.object=object;
	}
	
	void setOperatorVariable(OperatorVariable variable)
	{
	    this.variable=variable;
	}
	
	public ValidationResult set(String valueText) throws Throwable
	{
        Class<?> type = this.field.getType();
        Object value=null;
        
        try
        {
            if (type.isEnum())
            {
                value=valueText;
            }
            else if (type == String.class)
            {
                value=valueText;
            }
            else if (type == boolean.class)
            {
                value=Boolean.parseBoolean(valueText);
            }
            else if (type == byte.class)
            {
                value = Byte.parseByte(valueText);
            }
            else if (type == short.class)
            {
                value = Short.parseShort(valueText);
            }
            else if (type == int.class)
            {
                value= Integer.parseInt(valueText);
            }
            else if (type == long.class)
            {
                value = Long.parseLong(valueText);
            }
            else if (type == float.class)
            {
                value = Float.parseFloat(valueText);
            }
            else if (type == double.class)
            {
                value = Double.parseDouble(valueText);
            }
            else if (type == AtomicBoolean.class)
            {
                value = Boolean.parseBoolean(valueText);
            }
            else if (type == AtomicInteger.class)
            {
                value = Integer.parseInt(valueText);
            }
            else if (type == AtomicLong.class)
            {
                value = Long.parseLong(valueText);
            }
            else if (type == AtomicLong.class)
            {
                value = Double.parseDouble(valueText);
            }
            else
            {
                throw new Exception();
            }
        }
        catch (Throwable t)
        {
            return new ValidationResult(Status.VALIDATION_FAILED,null,"Parse failed: "+t.getMessage());
        }
        
        try
	    {
    	    ValidationResult result=this.validator.validate(this, value);
    	    if (result.getStatus()!=Status.SUCCESS)
    	    {
    	        return result;
    	    }
            value=result.getResult();
	    }
	    catch (Throwable t)
	    {
	        return new ValidationResult(Status.VALIDATION_FAILED,null,t.getMessage());
	    }
        
        if (type.isEnum())
        {
            try
            {
                this.field.set(this.object, Enum.valueOf((Class<Enum>) field.getType(), (String)valueText.toString()));
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == String.class)
        {
            this.field.set(this.object,value);
        }
        else if (type == boolean.class)
        {
            try
            {
                this.field.set(this.object, value);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == byte.class)
        {
            try
            {
                byte typeValue=(byte)value;
                if (variable.maximum().length() > 0)
                {
                    byte maximum = Byte.parseByte(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    byte minimum = Byte.parseByte(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
                    }
                }
                this.field.setByte(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == short.class)
        {
            try
            {
                short typeValue = (short)value;
                if (variable.maximum().length() > 0)
                {
                    short maximum = Short.parseShort(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    short minimum = Short.parseShort(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                this.field.setShort(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == int.class)
        {
            try
            {
                int typeValue=(int)value;
                if (variable.maximum().length() > 0)
                {
                    int maximum = Integer.parseInt(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    int minimum = Integer.parseInt(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                this.field.setInt(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == long.class)
        {
            try
            {
                long typeValue=(long)value;
                if (variable.maximum().length() > 0)
                {
                    long maximum = Long.parseLong(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    long minimum = Long.parseLong(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                this.field.setLong(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == float.class)
        {
            try
            {
                float typeValue=(float)value;
                if (variable.maximum().length() > 0)
                {
                    float maximum = Float.parseFloat(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    float minimum = Float.parseFloat(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                this.field.setFloat(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == double.class)
        {
            try
            {
                double typeValue=(double)value;
                if (variable.maximum().length() > 0)
                {
                    double maximum = Double.parseDouble(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    double minimum = Double.parseDouble(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                this.field.setDouble(this.object, typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == AtomicBoolean.class)
        {
            try
            {
                boolean typeValue=(boolean)value;
                ((AtomicBoolean) this.field.get(this.object)).set(typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == AtomicInteger.class)
        {
            try
            {
                int typeValue=(int)value;
                if (variable.maximum().length() > 0)
                {
                    int maximum = Integer.parseInt(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    int minimum = Integer.parseInt(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                ((AtomicInteger) this.field.get(this.object)).set(typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        else if (type == AtomicLong.class)
        {
            try
            {
                long typeValue=(long)value;
                if (variable.maximum().length() > 0)
                {
                    long maximum = Long.parseLong(variable.maximum());
                    if (typeValue > maximum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: maximum="+maximum+", value="+typeValue);
                    }
                }
                if (variable.minimum().length() > 0)
                {
                    long minimum = Long.parseLong(variable.minimum());
                    if (typeValue < minimum)
                    {
                        return new ValidationResult(Status.SET_FAILED,null,"Out of range: minimum="+minimum+", value="+typeValue);
                    }
                }
                ((AtomicLong) this.field.get(this.object)).set(typeValue);
            }
            catch (Throwable t)
            {
                return new ValidationResult(Status.SET_FAILED,null,"Set failed: type="+type.getSimpleName()+", value="+valueText);
            }
        }
        this.modified=System.currentTimeMillis();
        return new ValidationResult(Status.SUCCESS);
	}
	@SuppressWarnings("unchecked")
	public void setEnumValue(String value) throws Throwable
	{
		field.set(object, Enum.valueOf((Class<Enum>) field.getType(), value));
		this.modified=System.currentTimeMillis();
	}
	public Object getObject()
	{
		return object;
	}
	public Field getField()
	{
		return field;
	}
	public String getName()
	{
		return variable.alias().length()==0?field.getName():variable.alias();
	}
	public Object getValue() throws Throwable
	{
		return field.get(this.object);
	}
	public Object getDefaultValue()
	{
		return this.defaultValue;
	}
	public OperatorVariable getOperatorVariable()
	{
		return variable;
	}
	public long getModified()
	{
		return modified;
	}
}