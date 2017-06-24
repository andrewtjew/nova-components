package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.nova.collections.FileCache;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
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
    private HashSet<String> styleSet;
    private HashSet<String> baseSet;
    private HashSet<String> linkSet;
    private HashSet<String> metaSet;
    private HashSet<String> scriptSet;

    public Head()
    {
    }

    public Head add(String id, style style)
    {
        if (this.styleSet == null)
        {
            this.styleSet = new HashSet<>();
        }
        if (this.styleSet.add(id))
        {
            addInner(style);
        }
        return this;
    }

    public Head add(String id, base base)
    {
        if (this.baseSet == null)
        {
            this.baseSet = new HashSet<>();
        }
        if (this.baseSet.add(id))
        {
            addInner(base);
        }
        return this;
    }

    public Head add(String id, link link)
    {
        if (this.linkSet == null)
        {
            this.linkSet = new HashSet<>();
        }
        if (this.linkSet.add(id))
        {
            addInner(link);
        }
        return this;
    }

    public Head add(String id, meta meta)
    {
        if (this.metaSet == null)
        {
            this.metaSet = new HashSet<>();
        }
        if (this.metaSet.add(id))
        {
            addInner(meta);
        }
        return this;
    }

    public Head add(String id, script script)
    {
        if (this.scriptSet == null)
        {
            this.scriptSet = new HashSet<>();
        }
        if (this.scriptSet.add(id))
        {
            addInner(script);
        }
        return this;
    }

    public Head addCssLink(String URL)
    {
        return add(URL,new link().rel(link_rel.stylesheet).type("text/css").href(URL));
    }
    public Head addScript(String URL)
    {
        return add(URL,new script().src(URL));
    }
}