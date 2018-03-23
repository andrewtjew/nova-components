package org.nova.http.server;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Transformers
{

	final ArrayList<ContentReader<?>> contentReaders;
	final ArrayList<ContentWriter<?>> contentWriters;
	final ArrayList<ContentEncoder> contentEncoders;
	final ArrayList<ContentDecoder> contentDecoders;
	final ArrayList<Filter> filters;
	final ArrayList<Filter> bottomFilters;
	final ArrayList<Filter> topFilters;
	
	public Transformers()
	{
	    this.contentDecoders=new ArrayList<>();
	    this.contentEncoders=new ArrayList<>();
	    this.contentReaders=new ArrayList<>();
	    this.contentWriters=new ArrayList<>();
	    this.filters=new ArrayList<>();
	    this.bottomFilters=new ArrayList<>();
	    this.topFilters=new ArrayList<>();
	}

    public void add(ContentReader<?>...contentReaders)
    {
        for (ContentReader<?> contentReader:contentReaders)
        {
            if (this.getContentReader(contentReader.getClass())==null)
            {
                this.contentReaders.add(contentReader);
            }
        }
    }
    public void add(ContentWriter<?>...contentWriters)
    {
        for (ContentWriter<?> contentWriter:contentWriters)
        {
            if (this.getContentWriter(contentWriter.getClass())==null)
            {
                this.contentWriters.add(contentWriter);
            }
        }
    }
    public void add(ContentEncoder...contentEncoders)
    {
        for (ContentEncoder contentEncoder:contentEncoders)
        {
            if (this.getContentEncoder(contentEncoder.getClass())==null)
            {
                this.contentEncoders.add(contentEncoder);
            }
        }
    }
    public void add(ContentDecoder...contentDecoders)
    {
        for (ContentDecoder contentDecoder:contentDecoders)
        {
            if (this.getContentDecoder(contentDecoder.getClass())==null)
            {
                this.contentDecoders.add(contentDecoder);
            }
        }
    }
    public void add(Filter...filters)
    {
        for (Filter filter:filters)
        {
            if (this.getFilter(filter.getClass())==null)
            {
                this.filters.add(filter);
            }
        }
    }
	
	
	public Filter getFilter(Class<? extends Filter> type)
	{
		for (Filter item:this.filters)
		{
			if (item.getClass()==type)
			{
				return item;
			}
		}
		return null;
	}

	public ContentDecoder getContentDecoder(Class<? extends ContentDecoder> type)
	{
		for (ContentDecoder item:this.contentDecoders)
		{
			if (item.getClass()==type)
			{
				return item;
			}
		}
		return null;
	}
	
	public ContentEncoder getContentEncoder(Type type)
	{
		for (ContentEncoder item:this.contentEncoders)
		{
			if (item.getClass()==type)
			{
				return item;
			}
		}
		return null;
	}
	
	public ContentReader<?> getContentReader(Type type)
	{
		for (ContentReader<?> item:this.contentReaders)
		{
			if (item.getClass()==type)
			{
				return item;
			}
		}
		return null;
	}

	public ContentWriter<?> getContentWriter(Type type)
	{
		for (ContentWriter<?> item:this.contentWriters)
		{
			if (item.getClass()==type)
			{
				return item;
			}
		}
		return null;
	}
	public ContentReader<?> getContentReader(String mediaType)
	{
		for (ContentReader<?> item:this.contentReaders)
		{
			if (item.getMediaType().equals(mediaType))
			{
				return item;
			}
		}
		return null;
	}

	public ContentWriter<?> getContentWriter(String mediaType)
	{
		for (ContentWriter<?> item:this.contentWriters)
		{
			if (item.getMediaType().equals(mediaType))
			{
				return item;
			}
		}
		return null;
	}
}
