package org.nova.html.bootstrap4;

import org.nova.html.tags.script;

public class BootStrapUtils
{
    public static script readyToolTipScript()
    {
        return new script().addInner("$(document).ready(function(){$('[data-toggle=\"tooltip\"]').tooltip();});");
    }
    public static script readyPopoverTipScript()
    {
        return new script().addInner("$(document).ready(function(){$('[data-toggle=\"popover\"]').popover();});");
    }
}
