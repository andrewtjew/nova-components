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
package org.nova.collections;

import java.util.HashMap;

import org.nova.annotations.Description;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;

abstract public class ContentCache<KEY,VALUE>
{
	final private long maxAgeMs;
	final private long contentCapacity;
	private long totalContentSize;
	
	static class Node<KEY,VALUE>
	{
		final KEY key;
		final VALUE value;
		final long size; 
		Node<KEY,VALUE> previous;
		Node<KEY,VALUE> next;
		
        long accessed;
        long created;
		public Node(KEY key,VALUE value,long size)
		{
			this.size=size;
			this.value=value;
			this.key=key;
			this.created=this.accessed=System.currentTimeMillis();
		}
	}
	
	public static class ValueSize<VALUE>
	{
		final VALUE value;
		final long size;
        public ValueSize(VALUE value,long size)
        {
            this.value=value;
            this.size=size;
        }
        public ValueSize(VALUE value)
        {
            this.value=value;
            this.size=0;
        }
	}

	public ContentCache()
    {
	    this(0,0,0);
    }
	
	public ContentCache(int capacity,long maxAgeMs,long contentCapacity)
	{
		this.hits=new CountMeter();
		this.misses=new CountMeter();
		this.ageMisses=new CountMeter();
		this.sizeEvicts=new CountMeter();
		this.nodeMap=new HashMap<>();
		this.first=null;
		this.capacity=capacity;
		this.maxAgeMs=maxAgeMs;
		this.contentCapacity=contentCapacity;
	}
	
	final private HashMap<KEY,Node<KEY,VALUE>> nodeMap;
	
	@Description("Cache hits.")
	final private CountMeter hits;
	
	@Description("Cache misses due to key not found")
	final private CountMeter misses;
	
	@Description("Cache misses due to max age exceeded.")
	final private CountMeter ageMisses;
	
	@Description("Cache evicts due to max size exceeded.")
	final private CountMeter sizeEvicts;
	
	final private int capacity;
	private Node<KEY,VALUE> first;
	private Node<KEY,VALUE> last;
	

	public VALUE get(KEY key) throws Throwable
	{
		return get(null,key);
	}
	public VALUE get(Trace parent,KEY key) throws Throwable
	{
		synchronized(this.nodeMap)
		{
			Node<KEY,VALUE> node=this.nodeMap.get(key);
			if (node!=null)
			{
				long now=System.currentTimeMillis();
				if ((this.maxAgeMs>0)&&(now-node.created<this.maxAgeMs))
				{
    				if (node.previous!=null)
    				{
    					node.previous.next=node.next;
    					node.previous=null;
    					if (node.next!=null)
    					{
    						node.next.previous=node.previous;
    					}
    					else
    					{
    						this.last=node.previous;
    					}
    					node.next=this.first;
    					this.first=node;
    				}
    				node.accessed=now;
    				this.hits.increment();
    				return node.value;
				}
				remove(key);
			}
		}
		this.misses.increment();
		return fill(parent,key);
	}

	public VALUE fill(Trace parent,KEY key) throws Throwable
	{
		synchronized(this)
		{
			ValueSize<VALUE> valueSize=load(parent,key);
			return put(parent,key,valueSize);
		}
	}

	public VALUE put(Trace parent,KEY key,ValueSize<VALUE> valueSize) throws Throwable
    {
	    if (valueSize==null)
	    {
	        throw new Exception("No value for key:"+key);
	    }
        synchronized(this)
        {
            Node<KEY,VALUE> node=new Node<KEY,VALUE>(key,valueSize.value,valueSize.size);
            synchronized(this.nodeMap)
            {
                if (this.contentCapacity>0)
                {
                    while (this.totalContentSize+node.size>this.contentCapacity)
                    {
                        this.nodeMap.remove(this.last.key);
                        this.totalContentSize-=this.last.size;
                        this.sizeEvicts.increment();
                        this.last=last.previous;
                        if (this.last!=null)
                        {
                            this.last.next=null;
                        }
                        else
                        {
                            this.first=null;
                            break;
                        }
                    }
                }
                if ((this.capacity>0)&&(this.nodeMap.size()==this.capacity))
                {
                    this.nodeMap.remove(this.last.key);
                    this.totalContentSize-=this.last.size;
                    this.last=last.previous;
                    if (this.last!=null)
                    {
                        this.last.next=null;
                    }
                    else
                    {
                        this.first=null;
                    }
                }
                this.nodeMap.put(key, node);
                this.totalContentSize+=node.size;
                node.next=this.first;
                this.first=node;
                if (this.last==null)
                {
                    this.last=node;
                }
            }
            return valueSize.value;
        }
    }
	
	public VALUE remove(KEY key)
	{
		synchronized(this.nodeMap)
		{
			Node<KEY,VALUE> node=this.nodeMap.remove(key);
			if (node==null)
			{
				return null;
			}
			this.totalContentSize-=node.size;
			if (node.previous==null)
			{
				this.first=node.next;
			}
			else
			{
				node.previous.next=node.next;
			}
			if (node.next==null)
			{
				this.last=node.previous;
			}
			else
			{
				node.next.previous=node.previous;
			}
			return node.value;
		}
	}
	
	public void removeAll()
	{
		synchronized(this.nodeMap)
		{
			this.nodeMap.clear();
			this.last=this.first=null;
			this.totalContentSize=0;
		}		
	}
	
	public void resetMeters()
	{
		this.hits.set(0);
		this.misses.set(0);
		this.ageMisses.set(0);
	}
	
	public int getEntrySize()
	{
		synchronized(this.nodeMap)
		{
			return this.nodeMap.size();
		}
	}
	public long getTotalContentSize()
	{
		synchronized(this.nodeMap)
		{
			return this.totalContentSize;
		}
	}
	
	public CountMeter getHits()
	{
		return this.hits;
	}
	
	public CountMeter getMisses()
	{
		return this.misses;
	}
	public CountMeter getAgeMisses()
	{
		return this.ageMisses;
	}
	public CountMeter getSizeEvicts()
	{
		return this.sizeEvicts;
	}
	
	abstract protected ValueSize<VALUE> load(Trace parent,KEY key) throws Throwable; //Don't return null. return new ValueSize(null) instead. 
}
