package org.nova.collections;

import java.util.HashMap;

import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;

public abstract class LeastRecentlyUsedCache<KEY,VALUE>
{
	static class Node<KEY,VALUE>
	{
		final KEY key;
		final VALUE value;
		Node<KEY,VALUE> previous;
		Node<KEY,VALUE> next;
		public Node(KEY key,VALUE value)
		{
			this.value=value;
			this.key=key;
		}
	}
	
	final private HashMap<KEY,Node<KEY,VALUE>> nodeMap;
	final private CountMeter hits;
	final private CountMeter misses;
	final private int capacity;
	private Node<KEY,VALUE> first;
	private Node<KEY,VALUE> last;
	
	public LeastRecentlyUsedCache(int capacity) throws Exception
	{
		if (capacity<1)
		{
			throw new Exception("capacity="+capacity);
		}
		this.hits=new CountMeter();
		this.misses=new CountMeter();
		this.nodeMap=new HashMap<>();
		this.first=null;
		this.capacity=capacity;
	}

	private void makeRecent(Node<KEY,VALUE> node)
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
	}
	
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
				makeRecent(node);
				this.hits.increment();
				return node.value;
			}
		}
		this.misses.increment();
		return prefetch(parent,key);
	}

	public VALUE set(KEY key,VALUE value) throws Throwable
	{
		synchronized(this)
		{
			Node<KEY,VALUE> node=new Node<KEY,VALUE>(key,value);
			synchronized(this.nodeMap)
			{
				if (this.nodeMap.size()==this.capacity)
				{
					this.nodeMap.remove(this.last.key);
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
				node.next=this.first;
				this.first=node;
				if (this.last==null)
				{
					this.last=node;
				}
			}
			return value;
		}
	}

	public VALUE prefetch(Trace parent,KEY key) throws Throwable
    {
        VALUE value=load(parent,key);
        set(key,value);
        return value;
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
		}		
	}
	
	public void resetMeters()
	{
		this.hits.set(0);
		this.misses.set(0);
	}
	
	public int size()
	{
		synchronized(this.nodeMap)
		{
			return this.nodeMap.size();
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
	
	abstract protected VALUE load(Trace trace,KEY key) throws Throwable; 
}
