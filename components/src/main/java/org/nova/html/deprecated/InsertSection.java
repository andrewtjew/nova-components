package org.nova.html.deprecated;

import org.nova.html.deprecated.ElementMap;
import org.nova.html.deprecated.Section;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

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
            element.compose(composer);
        }
    }
	
}
