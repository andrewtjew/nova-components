package org.nova.html.tags.ext;

import org.nova.html.tags.span;
import org.nova.utils.Utils;

public class span_duration_hours extends span
{
    public span_duration_hours(long ms)
    {
        long hours=ms/(3600*1000);
        long minutes=(ms-hours*3600*1000)/(60*1000);
        long seconds=(ms-hours*3600*1000-minutes*60*1000)/1000;
        String text=String.format("%02d:%02d:%02d", hours,minutes,seconds);
        addInner(text);
        title(Utils.millisToNiceDurationString(ms));
    }
    
}
