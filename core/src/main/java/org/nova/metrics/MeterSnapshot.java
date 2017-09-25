package org.nova.metrics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.nova.annotations.Description;
import org.nova.core.Utils;

public class MeterSnapshot
{
	final private HashMap<String, HashMap<String, LevelMeterBox>> levelMeterCategories;
	final private HashMap<String, HashMap<String, RateMeterBox>> rateMeterCategories;
	final private HashMap<String, HashMap<String, CountMeterBox>> countMeterCategories;
	final private HashMap<String, HashMap<String, ValueRateMeterBox>> valueRateMeterCategories;

	MeterSnapshot(
			HashMap<String, HashMap<String, LevelMeterBox>> levelMeterCategories
			,HashMap<String, HashMap<String, RateMeterBox>> rateMeterCategories
			,HashMap<String, HashMap<String, CountMeterBox>> countMeterCategories
			,HashMap<String, HashMap<String, ValueRateMeterBox>> valueRateMeterCategories)
	{
		this.levelMeterCategories=levelMeterCategories;
		this.rateMeterCategories = rateMeterCategories;
		this.countMeterCategories = countMeterCategories;
		this.valueRateMeterCategories = valueRateMeterCategories;
	}

	public LevelMeterBox[] getLevelMeterBoxes()
	{
		int total = 0;
		for (HashMap<String, LevelMeterBox> value : this.levelMeterCategories.values())
		{
			total += value.size();
		}
		LevelMeterBox[] tracks = new LevelMeterBox[total];
		int index = 0;
		for (HashMap<String, LevelMeterBox> value : this.levelMeterCategories.values())
		{
			for (LevelMeterBox caller : value.values())
			{
				tracks[index++] = caller;
			}
		}
		return tracks;
	}

	public CountMeterBox[] getCountMeterBoxes()
	{
		int total = 0;
		for (HashMap<String, CountMeterBox> value : this.countMeterCategories.values())
		{
			total += value.size();
		}
		CountMeterBox[] tracks = new CountMeterBox[total];
		int index = 0;
		for (HashMap<String, CountMeterBox> value : this.countMeterCategories.values())
		{
			for (CountMeterBox caller : value.values())
			{
				tracks[index++] = caller;
			}
		}
		return tracks;
	}

	public RateMeterBox[] getRateMeterBoxes()
	{
		int total = 0;
		for (HashMap<String, RateMeterBox> value : this.rateMeterCategories.values())
		{
			total += value.size();
		}
		RateMeterBox[] tracks = new RateMeterBox[total];
		int index = 0;
		for (HashMap<String, RateMeterBox> value : this.rateMeterCategories.values())
		{
			for (RateMeterBox caller : value.values())
			{
				tracks[index++] = caller;
			}
		}
		return tracks;
	}

	public ValueRateMeterBox[] getValueRateMeterBoxes()
	{
		int total = 0;
		for (HashMap<String, ValueRateMeterBox> value : this.valueRateMeterCategories.values())
		{
			total += value.size();
		}
		ValueRateMeterBox[] tracks = new ValueRateMeterBox[total];
		int index = 0;
		for (HashMap<String, ValueRateMeterBox> value : this.valueRateMeterCategories.values())
		{
			for (ValueRateMeterBox caller : value.values())
			{
				tracks[index++] = caller;
			}
		}
		return tracks;
	}

	public String[] getCategories()
	{
		HashSet<String> set = new HashSet<>();
		set.addAll(this.rateMeterCategories.keySet());
		set.addAll(this.countMeterCategories.keySet());
		set.addAll(this.valueRateMeterCategories.keySet());
		return set.toArray(new String[set.size()]);
	}

	public CategoryMeters getMeterBoxes(String category)
	{
		LevelMeterBox[] levelMeterBoxes = null;
		HashMap<String, LevelMeterBox> levelMeters = this.levelMeterCategories.get(category);
		if (levelMeters != null)
		{
			levelMeterBoxes = new LevelMeterBox[levelMeters.size()];
			levelMeters.values().toArray(levelMeterBoxes);
		}

		RateMeterBox[] rateMeterBoxes = null;
		HashMap<String, RateMeterBox> rateMeters = this.rateMeterCategories.get(category);
		if (rateMeters != null)
		{
			rateMeterBoxes = new RateMeterBox[rateMeters.size()];
			rateMeters.values().toArray(rateMeterBoxes);
		}
		CountMeterBox[] countMeterBoxes = null;
		HashMap<String, CountMeterBox> countMeters = this.countMeterCategories.get(category);
		if (countMeters != null)
		{
			countMeterBoxes = new CountMeterBox[countMeters.size()];
			countMeters.values().toArray(countMeterBoxes);
		}
		ValueRateMeterBox[] ValueRateMeterBoxes = null;
		HashMap<String, ValueRateMeterBox> valueRateMeters = this.valueRateMeterCategories.get(category);
		if (valueRateMeters != null)
		{
			ValueRateMeterBoxes = new ValueRateMeterBox[valueRateMeters.size()];
			valueRateMeters.values().toArray(ValueRateMeterBoxes);
		}
		return new CategoryMeters(levelMeterBoxes,rateMeterBoxes, countMeterBoxes, ValueRateMeterBoxes);
	}


	public HashMap<String, HashMap<String, LevelMeterBox>> getLevelMeterCategories()
	{
		return levelMeterCategories;
	}


	public HashMap<String, HashMap<String, RateMeterBox>> getRateMeterCategories()
	{
		return rateMeterCategories;
	}


	public HashMap<String, HashMap<String, CountMeterBox>> getCountMeterCategories()
	{
		return countMeterCategories;
	}


	public HashMap<String, HashMap<String, ValueRateMeterBox>> getValueRateMeterCategories()
	{
		return valueRateMeterCategories;
	}
	
}
