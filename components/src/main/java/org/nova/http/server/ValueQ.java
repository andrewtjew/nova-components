package org.nova.http.server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ValueQ 
{
	static class ValueQComparator implements Comparator<ValueQ>
	{
		@Override
		public int compare(ValueQ arg0, ValueQ arg1)
		{
			return arg0.q<=arg1.q?1:-1;
		}
	}
	
	final String value;
	final double q;
	ValueQ(String value,double q)
	{
		this.value=value;
		this.q=q;
	}
	final static ValueQ.ValueQComparator COMPARATOR=new ValueQComparator();
	
	static List<ValueQ> sort(String accept)
	{
		String[] parts=org.nova.core.Utils.split(accept.trim(), ',');
		ArrayList<ValueQ> list=new ArrayList<>();
		for (String item:parts)
		{
			double q=1;
			String[] mediaTypeParameter=org.nova.core.Utils.split(item.trim(), ';');
			if (mediaTypeParameter.length>1)
			{
				String[] qvalue=org.nova.core.Utils.split(mediaTypeParameter[1].trim(), '=');
				if ((qvalue.length==2)&&("q".equals(qvalue[0])))
				{
					try
					{
						q=Double.parseDouble(qvalue[1]);
					}
					catch (NumberFormatException ex)
					{ 
						//Be tolerant...
					}
				}
			}
			else
			{
				list.add(new ValueQ(mediaTypeParameter[0],q));
			}
		}
		list.sort(COMPARATOR);
		return list;
	}
	
}