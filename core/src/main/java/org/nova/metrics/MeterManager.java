package org.nova.metrics;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.nova.annotations.Alias;
import org.nova.annotations.Description;
import org.nova.core.Utils;

public class MeterManager
{
	final private HashMap<String, HashMap<String, LevelMeterBox>> levelMeterCategories;
	final private HashMap<String, HashMap<String, RateMeterBox>> rateMeterCategories;
	final private HashMap<String, HashMap<String, CountMeterBox>> countMeterCategories;
	final private HashMap<String, HashMap<String, CountAverageRateMeterBox>> countAverageRateMeterCategories;

	public MeterManager()
	{
		this.levelMeterCategories=new HashMap<>();
		this.rateMeterCategories = new HashMap<>();
		this.countMeterCategories = new HashMap<>();
		this.countAverageRateMeterCategories = new HashMap<>();
	}

	public void register(Object object) throws Exception
	{
	    register(object.getClass().getSimpleName(),object);
	}
	
	public void register(String category, Object object) throws Exception
	{
		synchronized (this)
		{
			HashMap<String, LevelMeterBox> levelMeters = this.levelMeterCategories.get(category);
			if (levelMeters == null)
			{
				levelMeters = new HashMap<>();
				this.levelMeterCategories.put(category, levelMeters);
			}
			HashMap<String, RateMeterBox> rateMeters = this.rateMeterCategories.get(category);
			if (rateMeters == null)
			{
				rateMeters = new HashMap<>();
				this.rateMeterCategories.put(category, rateMeters);
			}
			HashMap<String, CountMeterBox> countMeters = this.countMeterCategories.get(category);
			if (countMeters == null)
			{
				countMeters = new HashMap<>();
				this.countMeterCategories.put(category, countMeters);
			}
			HashMap<String, CountAverageRateMeterBox> countAverageRateMeters = this.countAverageRateMeterCategories.get(category);
			if (countAverageRateMeters == null)
			{
				countAverageRateMeters = new HashMap<>();
				this.countAverageRateMeterCategories.put(category, countAverageRateMeters);
			}
			register(category, object.getClass(), object,levelMeters,rateMeters,countMeters,countAverageRateMeters);
		}
	}

	private void register(String category, Class<?> type, Object object
			,HashMap<String, LevelMeterBox> levelMeters 
			,HashMap<String, RateMeterBox> rateMeters 
			,HashMap<String, CountMeterBox> countMeters
			,HashMap<String, CountAverageRateMeterBox> countAverageRateMeters
			) throws Exception
	{
		Class<?> superClass = type.getSuperclass();
		if (superClass != null)
		{
			register(category, superClass, object,levelMeters,rateMeters,countMeters,countAverageRateMeters);
		}

		for (Field field : type.getDeclaredFields())
		{
			Alias alias= field.getAnnotation(Alias.class);
			Description description=field.getAnnotation(Description.class);
			String key = (alias != null ? alias.value() : field.getName());
			if (field.getType() == LevelMeter.class)
			{
				if (levelMeters.containsKey(key))
				{
					throw new Exception("Name conflict. Site=" + object.getClass().getName() + "." + field.getName());
				}
				field.setAccessible(true);
				levelMeters.put(key, new LevelMeterBox(category, key, description,(LevelMeter) field.get(object)));
			}
			else if (field.getType() == RateMeter.class)
			{
				if (rateMeters.containsKey(key))
				{
					throw new Exception("Name conflict. Site=" + object.getClass().getName() + "." + field.getName());
				}
				field.setAccessible(true);
				rateMeters.put(key, new RateMeterBox(category, key, description, (RateMeter) field.get(object)));
			}
			else if (field.getType() == CountMeter.class)
			{
				if (countMeters.containsKey(key))
				{
					throw new Exception("Name conflict. Site=" + object.getClass().getName() + "." + field.getName());
				}
				field.setAccessible(true);
				countMeters.put(key, new CountMeterBox(category, key, description, (CountMeter) field.get(object)));
			}
			else if (Utils.isDerivedFrom(field.getType(), CountAverageRateMeter.class))
			{
				if (countAverageRateMeters.containsKey(key))
				{
					throw new Exception("Name conflict. Site=" + object.getClass().getName() + "." + field.getName());
				}
				field.setAccessible(true);
				countAverageRateMeters.put(key, new CountAverageRateMeterBox(category, key, description,(CountAverageRateMeter) field.get(object)));
			}
		}
	}

	public MeterSnapshot getSnapshot()
	{
		synchronized (this)
		{
			return new MeterSnapshot(this.levelMeterCategories, this.rateMeterCategories, this.countMeterCategories, this.countAverageRateMeterCategories);
		}
	}

}
