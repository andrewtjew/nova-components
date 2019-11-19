/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
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
package org.nova.sqldb;

public class ClusterNode
{
	final private Connector[] connectors;
	final private int[] weights;
	final long[] selectedCounts;
	private int readConnectorIndex;
	private int accumulator;
	private int step;

	public ClusterNode(Connector readWriteConnector,Connector...readConnectors)
	{
		this(readWriteConnector,readConnectors,new int[1+readConnectors.length]);
	}
	public ClusterNode(Connector readWriteConnector,Connector[] readConnectors,int[] weights)
	{
		this.connectors=new Connector[1+readConnectors.length];
		this.selectedCounts=new long[1+readConnectors.length];
		this.weights=weights;
		this.connectors[0]=readWriteConnector;
		this.weights[0]=1;
		for (int i=0;i<readConnectors.length;i++)
		{
			this.connectors[1+i]=readConnectors[i];
			this.weights[1+i]=1;
		}
		this.accumulator=0;
		this.step=1;
		this.readConnectorIndex=0;
	}
	
	public Connector getReadWriteConnector()
	{
		return this.connectors[0];
	}
	public void setWeights(int readWriteWeight,int...readWeights) throws Exception
	{
		synchronized(this)
		{
			if (readWeights.length+1!=this.weights.length)
			{
				throw new Exception();
			}
			this.weights[0]=readWriteWeight;
			for (int i=0;i<readWeights.length;i++)
			{
				this.weights[1+i]=readWeights[i];
			}
			computeStepping();
		}
	}
	public void setReadWriteWeights(int readWriteWeight) throws Exception
	{
		synchronized(this)
		{
			this.weights[0]=readWriteWeight;
			computeStepping();
		}
	}
	public void setReadWeight(int index,int readWeight) throws Exception
	{
		if (index<0)
		{
			throw new Exception();
		}
		synchronized(this)
		{
			this.weights[1+index]=readWeight;
			computeStepping();
		}
	}
	public int getReadWriteWeight()
	{
		synchronized(this)
		{
			return this.weights[0];
		}
	}
	public int getReadWeight(int index) throws Exception
	{
		if (index<0)
		{
			throw new Exception();
		}
		synchronized(this)
		{
			return this.weights[1+index];
		}
	}
	
	private void computeStepping() throws Exception
	{
		int totalWeight=0;
		for (int i=0;i<this.weights.length;i++)
		{
			int weight=this.weights[i];
			if (weight<0)
			{
				throw new Exception();
			}
			totalWeight+=weight;
		}
		if (totalWeight==0)
		{
			throw new Exception();
		}

		this.step=Integer.MAX_VALUE;
		for (int i=0;i<this.weights.length;i++)
		{
			int weight=this.weights[i];
			if (this.step>weight)
			{
				this.step=weight;
			}
		}
		this.accumulator=0;
		this.readConnectorIndex=(this.readConnectorIndex+1)%this.connectors.length;
	}
	
	public Connector getReadConnector()
	{
		synchronized (this)
		{
			this.accumulator+=this.step;
			for (int weight=this.weights[this.readConnectorIndex];this.accumulator>weight;weight=this.weights[this.readConnectorIndex=(this.readConnectorIndex+1)%this.connectors.length])
			{
				this.accumulator-=weight;
			}
			this.selectedCounts[this.readConnectorIndex]++;
			return this.connectors[this.readConnectorIndex];
		}
	}
	public Connector getReadConnector(int index)
	{
		return this.connectors[1+index];
	}
	public long[] getSelectedCounts()
	{
		long[] results=new long[this.selectedCounts.length];
		synchronized (this)
		{
			for (int i=0;i<results.length;i++)
			{
				results[i]=this.selectedCounts[i];
			}
		
		}
		return results;
	}
	
}
