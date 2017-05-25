package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.HashMap;

import org.nova.collections.FileCache;
import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.tags.base;
import org.nova.html.tags.head;
import org.nova.html.tags.link;
import org.nova.html.tags.meta;
import org.nova.html.tags.title;
import org.nova.html.tags.style;
import org.nova.html.tags.script;
import org.nova.html.tags.noscript;

public class Head extends head
{
    private title title;
    private noscript noscript;
    final private HashMap<String,style> styles;
    final private HashMap<String,base> bases;
    final private HashMap<String,link> links;
    final private HashMap<String,meta> metas;
    final private HashMap<String,script> scripts;
    
    public Head()
    {
        this.styles=new HashMap<>();
        this.bases=new HashMap<>();
        this.links=new HashMap<>();
        this.metas=new HashMap<>();
        this.scripts=new HashMap<>();
    }
    
    public Head add(String id,style style)
    {
        this.styles.put(id, style);
        return this;
    }
    public Head add(String id,base base)
    {
        this.bases.put(id, base);
        return this;
    }
    public Head add(String id,link link)
    {
        this.links.put(id, link);
        return this;
    }
    public Head add(String id,meta meta)
    {
        this.metas.put(id, meta);
        return this;
    }
    public Head add(String id,script script)
    {
        this.scripts.put(id, script);
        return this;
    }
    public Head addScript(String URL)
    {
        if (this.scripts.containsKey(URL)==false)
        {
            this.scripts.put(URL, new script().src(URL));
        }
        return this;
    }
    public Head setTitle(title title)
    {
        this.title=title;
        return this;
    }
    public Head setTitle(noscript noscript)
    {
        this.noscript=noscript;
        return this;
    }
    
    @Override
    public void build(Builder builder) throws Throwable
    {
        if (this.title!=null)
        {
            addInner(this.title);
        }
        for (style value:this.styles.values())
        {
            addInner(value);
        }
        for (base value:this.bases.values())
        {
            addInner(value);
        }
        for (link value:this.links.values())
        {
            addInner(value);
        }
        for (meta value:this.metas.values())
        {
            addInner(value);
        }
        for (script value:this.scripts.values())
        {
            addInner(value);
        }
        if (this.noscript!=null)
        {
            addInner(this.noscript);
        }
        super.build(builder);
    }
}
