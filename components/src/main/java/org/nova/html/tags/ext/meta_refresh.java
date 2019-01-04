package org.nova.html.tags.ext;

import org.nova.html.enums.http_equiv;
import org.nova.html.tags.meta;

public class meta_refresh extends meta
{
    public meta_refresh(int seconds)
    {
        http_equiv_content(http_equiv.refresh,Integer.toString(seconds));
    }
    
}
