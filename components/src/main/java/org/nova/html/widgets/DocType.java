package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;

public class DocType extends Element
{
    final private String docType;
    public DocType(String docType)
    {
        this.docType=docType;
    }
    @Override
    public void build(Builder builder) throws Throwable
    {
        builder.getOutputStream().write(("<!DOCTYPE "+docType+">").getBytes(StandardCharsets.UTF_8));
    }

}
