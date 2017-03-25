package org.nova.collections;

import java.util.HashMap;

import org.nova.annotations.Description;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;

abstract public class ContentCache<KEY,VALUE>
{
	final private long maxAge;
	final private long maxSize;
	private long totalContentSize;
	
	static class Node<KEY,VALUE>
	{
		final KEY key;
		final VALUE value;
		final long size;
		Node<KEY,VALUE> previous;
		Node<KEY,VALUE> next;
		
		long accessed;
		public Node(KEY key,VALUE value,long size)
		{
			this.size=size;
			this.value=value;
			this.key=key;
			this.accessed=System.currentTimeMillis();
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
	}

	public ContentCache(int capacity,long maxAge,long maxSize) throws Exception
	{
		if (capacity<1)
		{
			throw new Exception("capacity="+capacity);
		}
		this.hits=new CountMeter();
		this.misses=new CountMeter();
		this.ageMisses=new CountMeter();
		this.sizeEvicts=new CountMeter();
		this.nodeMap=new HashMap<>();
		this.first=null;
		this.capacity=capacity;
		this.maxAge=maxAge;
		this.maxSize=maxSize;
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
				if (now-node.accessed>this.maxAge)
				{
					if (node.previous!=null)
					{
						node.previous.next=null;
					}
					this.last=node.previous;
					if (this.first==node)
					{
						this.first=null;
					}
					int totalAged=0;
					do
					{
						this.nodeMap.remove(node.key);
						this.totalContentSize-=node.size;
						totalAged++;
						node=node.next;
					}
					while (node.next==null);
					this.ageMisses.add(totalAged);
				}
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
		}
		this.misses.increment();
		return preload(parent,key);
	}

	public VALUE preload(Trace parent,KEY key) throws Throwable
	{
		synchronized(this)
		{
			ValueSize<VALUE> valueSize=load(parent,key);
			Node<KEY,VALUE> node=new Node<KEY,VALUE>(key,valueSize.value,valueSize.size);
			synchronized(this.nodeMap)
			{
				while (this.totalContentSize+node.size>this.maxSize)
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
				if (this.nodeMap.size()==this.capacity)
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
	
	
	abstract protected ValueSize<VALUE> load(Trace trace,KEY key) throws Throwable; 


}
