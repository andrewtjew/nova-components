package org.nova.sqldb;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldMaps
{
	private static HashMap<String,HashMap<String,Field>> maps=new HashMap<>();
	
	public static Map<String,Field> get(Class<?> type)
	{
		String key=type.getCanonicalName();
		synchronized (maps)
		{
			Map<String,Field> map=maps.get(key);
			if (map!=null)
			{
				return map;
			}
		}
		HashMap<String,Field> map=new HashMap<>();
		for (Field field:type.getDeclaredFields())
		{
			ColumnNameAlias alias=(ColumnNameAlias)field.getAnnotation(ColumnNameAlias.class);
			String name=alias!=null?alias.value():field.getName();
			field.setAccessible(true);
			map.put(name, field);
		}
		synchronized (maps)
		{
			maps.put(key,map);
		}
		return map;
	}
}
