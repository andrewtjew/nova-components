package org.nova.html.widgets.templates;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.widgets.Text;

public class Template extends Element 
{
    final Section[] sections;
    final ElementMap map;
    
    Template(Section[] sections)
    {
        this.sections=sections;
        this.map=new ElementMap();
    }
    
    public Template(Template template)
    {
        this.sections=template.sections;
        this.map=new ElementMap();
        this.map.putAll(template.map);
    }
    
    public Template(Element element) throws Throwable
    {
        this(TemplateComposer.compose(element));
    }
    
    public void fill(String key,Element element)
    {
        this.map.put(key, element);
    }

    public void fill(String key,String text)
    {
        this.map.put(key, new Text(text));
    }
    public Template copy()
    {
        return new Template(this);
    }


    @Override
    public void compose(Composer composer) throws Throwable
    {
        for (Section section:this.sections)
        {
            section.write(composer, this.map);
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        for (Section section:this.sections)
        {
            sb.append(section);
        }
        return sb.toString();
    }
    
    
}
