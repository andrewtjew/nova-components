package org.nova.html.ext;

import org.nova.html.tags.body;
import org.nova.html.tags.html;

public class Location extends html
{
    public Location(String location)
    {
        this.returnAddInner(new body()).onload("window.location='"+location+"'");
    }
}
