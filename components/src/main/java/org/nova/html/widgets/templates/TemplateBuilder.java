package org.nova.html.widgets.templates;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;

public class TemplateBuilder extends Builder
{
    final private ArrayList<Section> sections;
    private ByteArrayOutputStream outputStream;
    
    public TemplateBuilder()
    {
        this.sections=new ArrayList<>();
    }
    
    public void processInsertKey(InsertKey insertKey) throws IOException
    {
        this.outputStream.close();
        this.sections.add(new StaticSection(this.outputStream.toByteArray()));
        this.sections.add(new InsertSection(insertKey.getKey()));
        
        this.outputStream=new ByteArrayOutputStream();
    }
    
    public Template build(Element element) throws Throwable
    {
        this.outputStream=new ByteArrayOutputStream();
        element.build(this);
        if (this.outputStream.size()>0)
        {
            this.sections.add(new StaticSection(this.outputStream.toByteArray()));
        }
        return new Template(this.sections.toArray(new Section[this.sections.size()]));
    }
    
    @Override
    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }
}
