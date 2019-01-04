package org.nova.html.templates.deprecated;

import org.nova.html.elements.Composer;
import org.nova.html.templates.deprecated.ElementMap;
import org.nova.html.templates.deprecated.Section;

class StaticSection extends Section
{
    final private String text;
    StaticSection(String text)
    {
        this.text=text;
    }
    @Override
    public void write(Composer composer,ElementMap map) throws Throwable
    {
        composer.getStringBuilder().append(this.text);
    }
    @Override 
    public String toString()
    {
        return this.text;
    }
}
