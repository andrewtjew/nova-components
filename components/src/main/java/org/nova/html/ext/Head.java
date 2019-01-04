package org.nova.html.ext;

import java.util.HashSet;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.base;
import org.nova.html.tags.link;
import org.nova.html.tags.meta;
import org.nova.html.tags.style;
import org.nova.html.tags.script;

public class Head extends GlobalEventTagElement<Head>
{
    private HashSet<String> styleSet;
    private HashSet<String> baseSet;
    private HashSet<String> linkSet;
    private HashSet<String> metaSet;
    private HashSet<String> scriptSet;

    public Head()
    {
        super("head");
    }

    public Head add(String key, style style)
    {
        if (this.styleSet == null)
        {
            this.styleSet = new HashSet<>();
        }
        if (this.styleSet.add(key))
        {
            this.addInner(style);
        }
        return this;
    }

    public Head add(String key, base base)
    {
        if (this.baseSet == null)
        {
            this.baseSet = new HashSet<>();
        }
        if (this.baseSet.add(key))
        {
            this.addInner(base);
        }
        return this;
    }

    public Head add(String key, link link)
    {
        if (this.linkSet == null)
        {
            this.linkSet = new HashSet<>();
        }
        if (this.linkSet.add(key))
        {
            this.addInner(link);
        }
        return this;
    }

    public Head add(String key, meta meta)
    {
        if (this.metaSet == null)
        {
            this.metaSet = new HashSet<>();
        }
        if (this.metaSet.add(key))
        {
            this.addInner(meta);
        }
        return this;
    }

    public Head add(String key, script script)
    {
        if (this.scriptSet == null)
        {
            this.scriptSet = new HashSet<>();
        }
        if (this.scriptSet.add(key))
        {
            this.addInner(script);
        }
        return this;
    }

    public Head includeCss(String URL)
    {
        return add(URL,new link().rel(link_rel.stylesheet).type("text/css").href(URL));
    }
    public Head includeScript(String URL)
    {
        return add(URL,new script().src(URL));
    }
    
}
