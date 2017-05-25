package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
