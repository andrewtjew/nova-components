package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;

public class InsertKey extends Element
{
    final private String key;
    public InsertKey(String key)
    {
        this.key=key;
    }
    @Override
    public void build(Builder builder) throws Throwable
    {
        if (builder instanceof TemplateBuilder)
        {
            ((TemplateBuilder)builder).processInsertKey(this);
        }
    }
    public String getKey()
    {
        return this.key;
    }
    
}
