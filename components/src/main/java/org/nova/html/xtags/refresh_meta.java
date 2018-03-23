package org.nova.html.xtags;

import org.nova.html.enums.http_equiv;
import org.nova.html.tags.meta;

public class refresh_meta extends meta
{
    public refresh_meta(int seconds)
    {
        http_equiv_content(http_equiv.refresh,Integer.toString(seconds));
    }
    
}
