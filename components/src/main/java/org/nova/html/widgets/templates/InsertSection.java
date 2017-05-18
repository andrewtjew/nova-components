package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.elements.OutputStreamBuilder;

public class InsertSection extends Section
{
    final private String key;
    InsertSection(String key)
    {
        this.key=key;
    }
    @Override
    public void write(OutputStream outputStream,ElementMap map) throws Throwable
    {
        Element element=map.get(this.key);
        if (element!=null)
        {
            element.build(new OutputStreamBuilder(outputStream));
        }
    }
	
}
