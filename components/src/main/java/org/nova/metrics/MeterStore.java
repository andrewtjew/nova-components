package org.nova.metrics;

import java.lang.reflect.Field;
import java.util.List;

import org.nova.annotations.Alias;
import org.nova.annotations.Description;
import org.nova.annotations.Metrics;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LongValueMeter;
import org.nova.metrics.RateMeter;
import org.nova.metrics.RecentSourceEventMeter;
import org.nova.pathStore.Store;

public class MeterStore
{
    final Store<MeterAttribute,Object,MeterAttributeValue> store;

	public MeterStore()
	{
	    this.store=new Store<>();
	}

    private void add(String path,String description,Object meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }

    private void addMeters(String path,Class<?> type,Object object) throws Exception
    {
        Class<?> superClass = type.getSuperclass();
        if (superClass != null)
        {
            addMeters(path, superClass, object);
        }

        for (Field field : type.getDeclaredFields())
        {
            field.setAccessible(true);
            Class<?> fieldType=field.getType();
            if (fieldType.isPrimitive())
            {
                continue;
            }

            Object fieldObject=field.get(object);
            if (fieldObject==null)
            {
                continue;
            }
            
            Alias alias= field.getAnnotation(Alias.class);
            String name = (alias != null ? alias.value() : field.getName());
            if ((fieldType == CountMeter.class)||(fieldType == LevelMeter.class)||(fieldType == RateMeter.class)||(fieldType == LongValueMeter.class))
            {
                Description description=field.getAnnotation(Description.class);
                field.setAccessible(true);
                add(path+"/"+name,description!=null?description.value():null,fieldObject);
            }
            else
            {
                Metrics metrics= field.getAnnotation(Metrics.class);
                if (metrics!=null)
                {
                    addMeters(path+"/"+name,fieldType,fieldObject);
                }
            }
        }
    }
    
    
    public void addMeters(String path,Object object) throws Exception
    {
        if (object==null)
        {
            return;
        }
        addMeters(path,object.getClass(),object);
    }

    public void addMeters(Object object) throws Exception
    {
        if (object==null)
        {
            return;
        }
        addMeters("/"+object.getClass().getSimpleName(),object.getClass(),object);
    }
    public void add(String path,String description,CountMeter meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }
    public void add(String path,String description,LevelMeter meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }
    public void add(String path,String description,RateMeter meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }
    public void add(String path,String description,LongValueMeter meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }
    public void add(String path,String description,RecentSourceEventMeter meter) throws Exception
    {
        this.store.add(path,new MeterAttributeValue(description, Thread.currentThread().getStackTrace(),path, meter));
    }
    
    public MeterAttributeValue getMeterAttributeValue(String path) throws Exception
    {
        return this.store.get(path);
    }

    public List<MeterAttributeValue> getAllMeterAttributeValues(String path) throws Exception
    {
        return this.store.getIncludingBelow(path);
    }
    
    public void remove(String path) throws Exception
    {
        this.store.remove(path);
    }


}
