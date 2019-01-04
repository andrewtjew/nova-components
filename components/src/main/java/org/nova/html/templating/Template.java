package org.nova.html.templating;

import java.util.ArrayList;
import java.util.List;

import org.nova.html.elements.Element;

public class Template
{
    final List<Element> elements;
    
    public Template(Element element) throws Throwable
    {
        TemplateComposer composer=new TemplateComposer();
        element.compose(composer);
        this.elements=composer.elements;
    }

    public Template(String htmlText) throws Throwable
    {
        this.elements=new Parser().parseText(htmlText);
    }
}
