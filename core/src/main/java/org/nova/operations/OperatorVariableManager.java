/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.operations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OperatorVariableManager
{
	final private HashMap<String, HashMap<String, VariableInstance>> map;
	final private HashMap<String,Validator> validators;
	
	
	public OperatorVariableManager()
	{
		this.map=new HashMap<>();
		this.validators=new HashMap<>();
		registerValidator(new DefaultValidator());
	}
    public void registerValidator(Validator validator) 
    {
        this.validators.put(validator.getClass().getName(),validator);
    }
	public void register(Object object) throws Throwable
	{
		register(object.getClass().getSimpleName(),object);
	}
	public void register(String category,Object object) throws Throwable
	{
		HashMap<String,VariableInstance> variables=map.get(category);
		if (variables==null)
		{
			variables=new HashMap<>();
			map.put(category, variables);
		}
		for (Field field:object.getClass().getDeclaredFields())
		{
			Class<?> type=field.getType();
			OperatorVariable variable=(OperatorVariable)field.getAnnotation(OperatorVariable.class);
			if (variable==null)
			{
				continue;
			}
			if (Modifier.isFinal(field.getModifiers()))
			{
				throw new Exception("OperatorVariable field cannot be final. Name="+object.getClass().getCanonicalName()+"."+field.getName()+", type="+type.getName());
			}
			if ((type.isPrimitive()==false)&&(type!=String.class)&&(type.isEnum()==false)&&(type!=AtomicInteger.class)&&(type!=AtomicLong.class)&&(type!=AtomicBoolean.class))
			{
				throw new Exception("OperatorVariable annotation must be of the following types only: primitives, String, enum, AtomicBoolean, AtomicInteger, AtomicLong, AtomicDouble. Name="+object.getClass().getCanonicalName()+"."+field.getName()+", type="+type.getName());
			}
			String key=variable.alias().length()==0?field.getName():variable.alias();
			if (variables.containsKey(key))
			{
				throw new Exception("OperatorVariable already registered: name="+object.getClass().getCanonicalName()+"."+field.getName()+", type="+type.getName()+", key="+key);
			}
			Validator validator=this.validators.get(variable.validator().getName());
            if (validator==null)
            {
                throw new Exception("No validator registered: name="+object.getClass().getCanonicalName()+"."+field.getName()+", type="+type.getName()+", key="+key);
            }
			variables.put(key,new VariableInstance(validator,variable, object, field));
		}
	}
	public VariableInstance getInstance(String category,String key)
	{
		synchronized (this)
		{
			HashMap<String, VariableInstance> variables=this.map.get(category);
			if (variables==null)
			{
				return null;
			}
			return variables.get(key);
		}
	}
    public boolean setOperatorVariable(String category,String key,OperatorVariable variable)
    {
        synchronized (this)
        {
            HashMap<String, VariableInstance> variables=this.map.get(category);
            if (variables==null)
            {
                return false;
            }
            VariableInstance instance=variables.get(key);
            if (instance==null)
            {
                return false;
            }
            instance.setOperatorVariable(variable);
            return true;
        }
    }
	public String[] getCategories()
	{
		synchronized (this)
		{
			String[] categories=new String[this.map.size()];
			int index=0;
			for (String category:this.map.keySet())
			{
				categories[index++]=category;
			}
			return categories;
		}
	}
	public VariableInstance[] getInstances(String category)
	{
		synchronized (this)
		{
			HashMap<String, VariableInstance> variables=this.map.get(category);
			if (variables==null)
			{
				return null;
			}
			return variables.values().toArray(new VariableInstance[variables.size()]);
		}
	}
	public String[] getKeys(String category)
	{
		synchronized (this)
		{
			HashMap<String, VariableInstance> variables=this.map.get(category);
			if (variables==null)
			{
				return null;
			}
			return variables.keySet().toArray(new String[variables.size()]);
		}
	}

}
