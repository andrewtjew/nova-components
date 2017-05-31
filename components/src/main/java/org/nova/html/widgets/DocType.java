package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

public class DocType extends Element
{
    final private String docType;
    public DocType(String docType)
    {
        this.docType=docType;
    }
    @Override
    public void compose(Composer builder) throws Throwable
    {
        builder.getStringBuilder().append("<!DOCTYPE "+docType+">");
    }

}
