package org.nova.html.ext;
import org.nova.html.tags.html;
import org.nova.html.tags.meta;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.http_equiv;
import org.nova.html.tags.head;

public class Redirect extends Element
{
    final private html html;
    
    public Redirect(String url)
    {
        this.html=new html();
        head head=this.html.returnAddInner(new head());
        meta meta=head.returnAddInner(new meta());
        meta.http_equiv_content(http_equiv.refresh,"0;url="+url);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.html);
    }
    
}
