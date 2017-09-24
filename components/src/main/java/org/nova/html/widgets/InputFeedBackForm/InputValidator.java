package org.nova.html.widgets.InputFeedBackForm;

import java.util.ArrayList;

import com.google.common.base.Strings;
import com.nova.reflect.FieldMap;
import com.nova.reflect.Fields;

public class InputValidator
{
    final private ArrayList<InputFeedback> inputFeedbacks;
    final private Object object;
    final private FieldMap fieldMap; 
    
    public InputValidator(Object object)
    {
        this.inputFeedbacks=new ArrayList<>();
        this.fieldMap=Fields.getFieldsMap(object.getClass());
        this.object=object;
    }
    
    private Object getValue(String id) throws Throwable
    {
        return this.fieldMap.get(id).get(this.object);
    }

    
    public String getRequiredString(String id,String feedback) throws Throwable
    {
        String value=null;
        Object valueObject=getValue(id);
        try
        {
            value=(String)valueObject;
            if ((value==null)||(value.length()==0))
            {
                this.inputFeedbacks.add(new InputFeedback(id,feedback));
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }

    public String getRequiredString(String id,int minLength,int maxLength,String feedback) throws Throwable
    {
        String value=null;
        Object valueObject=getValue(id);
        try
        {
            value=(String)valueObject;
            if (value==null)
            {
                this.inputFeedbacks.add(new InputFeedback(id,feedback));
            }
            if (minLength>=0)
            {
                if (value.length()<minLength)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,feedback));
                }
            }
            if (maxLength>=0)
            {
                if (value.length()>maxLength)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,feedback));
                }
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }
    public String getRequiredString(String id,int minLength,int maxLength) throws Throwable
    {
        String value=null;
        Object valueObject=getValue(id);
        try
        {
            if (valueObject==null)
            {
                this.inputFeedbacks.add(new InputFeedback(id,"String must not be null."));
            }
            value=valueObject.toString();
            if (minLength>=0)
            {
                if (value.length()<minLength)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Minimum length requirement of "+minLength+" characters not met."));
                }
            }
            if (maxLength>=0)
            {
                if (value.length()>maxLength)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Maximum length requirement of "+maxLength+" characters exceeded."));
                }
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,"Invalid format."));
        }
        return value;
    }    
    public String getRequiredString(String id) throws Throwable
    {
        return getRequiredString(id,"Required");
    }    

    public String getString(String id,String feedback) throws Throwable
    {
        String value=null;
        Object valueObject=getValue(id);
        try
        {
            if (valueObject!=null)
            {
                value=valueObject.toString();
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }    
    public String getString(String id) throws Throwable
    {
        return getString(id,"String expected.");
    }    

    public double getDouble(String id,String feedback) throws Throwable
    {
        double value=0;
        Object valueObject=getValue(id);
        try
        {
            value=(double)valueObject;
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }    

    public double getDouble(String id) throws Throwable
    {
        return getDouble(id,"Number expected.");
    }    
    public long getLong(String id,String feedback) throws Throwable
    {
        long value=0;
        Object valueObject=getValue(id);
        try
        {
            value=(long)valueObject;
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }    
    public long getLong(String id) throws Throwable
    {
        return getLong(id,"Number expected.");
    }    

    public long parseLong(String id,Long min,Long max) throws Throwable
    {
        long value=0;
        Object valueObject=getValue(id);
        try
        {
            value=Long.parseLong(valueObject.toString());
            if (min!=null)
            {
                if (value<min)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Minimum is "+min+"."));
                }
            }
            if (max!=null)
            {
                if (value>max)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Maximum is "+max+"."));
                }
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,"Integer (long) value expected."));
        }
        return value;
    }    
    
    public int parseInteger(String id,Integer min,Integer max) throws Throwable
    {
        int value=0;
        Object valueObject=getValue(id);
        try
        {
            value=Integer.parseInt(valueObject.toString());
            if (min!=null)
            {
                if (value<min)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Minimum is "+min+"."));
                }
            }
            if (max!=null)
            {
                if (value>max)
                {
                    this.inputFeedbacks.add(new InputFeedback(id,"Maximum is "+max+"."));
                }
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,"Integer (int) value expected."));
        }
        return value;
    }    
    
    
    public double parseDouble(String id,String feedback) throws Throwable
    {
        double value=0;
        Object valueObject=getValue(id);
        try
        {
            value=Double.parseDouble(valueObject.toString());
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }    

    public double parseDouble(String id) throws Throwable
    {
        return parseDouble(id,"Number expected.");
    }    
    public Double parseNullableDouble(String id,String feedback) throws Throwable
    {
        Double value=null;
        Object valueObject=getValue(id);
        try
        {
            if (valueObject!=null)
            {
                String text=valueObject.toString();
                if (text.length()==0)
                {
                    return null;
                }
                value=Double.parseDouble(text);
            }
        }
        catch (Throwable t)
        {
            this.inputFeedbacks.add(new InputFeedback(id,feedback));
        }
        return value;
    }    

    public Double parseNullableDouble(String id) throws Throwable
    {
        return parseNullableDouble(id,"Number expected.");
    }    

    public int getNumberOfValidationErrors()
    {
        return this.inputFeedbacks.size();
    }
    
    public FormResponse getErrorFormResponse()
    {
        return FormResponse.error(this.inputFeedbacks);
    }
}
