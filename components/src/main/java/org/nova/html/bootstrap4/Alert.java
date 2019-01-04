package org.nova.html.bootstrap4;

import org.nova.html.tags.button_button;

public class Alert extends StyleComponent<Alert>
{
    public Alert()
    {
        super("div","alert");
    }

    public Alert dismissible()
    {
        addClass("alert-dismissible");
        addInner(new button_button().addClass("close").attr("data-dismiss","alert").addInner("&times;"));
        return this;
    }

    public Alert fade()
    {
        addClass("fade show");
        return this;
    }

}
