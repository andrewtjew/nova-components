package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.script;

public class ClearTimeout extends Element
{
    final private script script;
    public ClearTimeout(String timerName)
    {
        this.script=new script().addInner("window.clearTimeout("+timerName+");");
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.getStringBuilder().append(script.toString());
        
    }

}
