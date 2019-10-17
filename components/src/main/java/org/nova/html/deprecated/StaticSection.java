package org.nova.html.deprecated;

import org.nova.html.deprecated.ElementMap;
import org.nova.html.deprecated.Section;
import org.nova.html.elements.Composer;

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
