package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.stream.events.Characters;

import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;

public class XYChart extends InnerElement<XYChart>
{
	final private String id;
//	private int width=100;
//	private int height=100;
	
	static class Point
	{
		final double x;
		final double y;
		
		Point(double x,double y)
		{
			this.x=x;
			this.y=y;
		}
		
	}
	private ArrayList<Point> points;
	
	public XYChart(String id)
	{
		this.id=id;
		this.points=new ArrayList<>();
	}
	
	/*
	
	public SeriesChart width(int width)
	{
		this.width=width;
		return this;
	}
	
	public SeriesChart height(int height)
	{
		this.height=height;
		return this;
	}
	*/
	
	public XYChart point(double x,double y)
	{
		this.points.add(new Point(x, y));
		return this;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		String chartId="chart"+this.id;
		String contextId="context"+this.id;
		
		sb.append("<script>var "+contextId+"=document.getElementById('"
		+this.id+"');var "+chartId+"=new Chart("+contextId+",{type:'line',data:{xLabels:['M', 'T', 'W'],datasets: [{data: [");
		Boolean commaNeeded=false;
		for (Point point:points)
		{
			if (commaNeeded==false)
			{
				commaNeeded=true;
				sb.append("{x:"+point.x+",y:"+point.y+"}");
			}
			else
			{
				sb.append(",{x:"+point.x+",y:"+point.y+"}");
				
			}
		}
		sb.append("]}]},options:{scales:{xAxes:[{type:'linear',position:'bottom'}]}}  });</script>");
		return sb.toString();
	}

    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(toString().getBytes(StandardCharsets.UTF_8));
    }
	
	
}
