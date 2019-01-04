package org.nova.html.templates.deprecated;

import java.io.IOException;
import java.util.ArrayList;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.templates.deprecated.InsertKey;
import org.nova.html.templates.deprecated.InsertSection;
import org.nova.html.templates.deprecated.Section;
import org.nova.html.templates.deprecated.StaticSection;
import org.nova.html.templates.deprecated.Template;
import org.nova.html.templates.deprecated.TemplateComposer;

public class TemplateComposer extends Composer
{
    final private ArrayList<Section> sections;
    private StringBuilder sb;
    
    public TemplateComposer()
    {
        this.sections=new ArrayList<>();
    }
    
    public void processInsertKey(InsertKey insertKey) throws IOException
    {
        this.sections.add(new StaticSection(this.sb.toString()));
        this.sections.add(new InsertSection(insertKey.getKey()));
        this.sb=new StringBuilder();
    }
    
    private Template compose_(Element element) throws Throwable 
    {
        this.sb=new StringBuilder(); 
        element.compose(this);
        if (this.sb.length()>0)
        {
            this.sections.add(new StaticSection(this.sb.toString()));
        }
        return new Template(this.sections.toArray(new Section[this.sections.size()]));
    }

    @Override
    public StringBuilder getStringBuilder()
    {
        return this.sb;
    }

    static public Template build(Element element) throws Throwable
    {
        return new TemplateComposer().compose_(element);
    }
}
