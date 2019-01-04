package org.nova.html.tags.ext;

import org.nova.html.enums.http_equiv;
import org.nova.html.tags.meta;

public class meta_redirect extends meta
{
    public meta_redirect(int seconds,String url)
    {
        http_equiv_content(http_equiv.refresh,Integer.toString(seconds));
        content(url);
    }
    
}
