package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.StringComposer;

public class InsertSection extends Section
{
    final private String key;
    InsertSection(String key)
    {
        this.key=key;
    }
    @Override
    public void write(Composer composer,ElementMap map) throws Throwable
    {
        Element element=map.get(this.key);
        if (element!=null)
        {
            element.build(composer);
        }
    }
	
}
