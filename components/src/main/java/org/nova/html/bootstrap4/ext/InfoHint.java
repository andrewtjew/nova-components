package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.BootStrapUtils;
import org.nova.html.bootstrap4.ClassBuilder;
import org.nova.html.bootstrap4.classes.Font;
import org.nova.html.bootstrap4.classes.Rounded;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.ext.Head;

public class InfoHint extends Span
{
    public InfoHint(Head head,String key,String hintText,String text)
    {
        tooltip(hintText);
        addInner(text);
        if (head!=null)
        {
            head.add(key, BootStrapUtils.readyToolTipScript());
        }
    }
    public InfoHint(Head head,String key,String hintText)
    {
        this(head,key,hintText,"i");
        color(StyleColor.light).border(StyleColor.info);
        px(2).mx(1);
        rounded(Rounded.circle);
        border();
        addClass(new ClassBuilder().font(Font.weight_bold));
    }
    public InfoHint(String hintText)
    {
        this(null,null,hintText);
    }
}
