package org.nova.html.xtags;

import org.nova.html.tags.th;

public class th_title extends th
{
    public th_title(Object inner,String title)
    {
        addInner(inner);
        title(title);
    }
    
}
