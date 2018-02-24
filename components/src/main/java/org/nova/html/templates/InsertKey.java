package org.nova.html.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.templates.TemplateComposer;

public class InsertKey extends Element
{
    final private String key;
    public InsertKey(String key)
    {
        this.key=key;
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (composer instanceof TemplateComposer)
        {
            ((TemplateComposer)composer).processInsertKey(this);
        }
    }
    public String getKey()
    {
        return this.key;
    }
    
}
