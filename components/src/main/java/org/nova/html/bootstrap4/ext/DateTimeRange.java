package org.nova.html.bootstrap4.ext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.nova.html.tags.form;
import org.nova.html.tags.time;

public class DateTimeRange
{
    final private DateTimeFormatter formatter;
    final private String from;
    final private String to;
    final private TimeZone timeZone;
    static final String divider=" - ";
    
    public DateTimeRange(String range,TimeZone timeZone,DateTimeFormatter formatter) throws Throwable
    {
        this.formatter=formatter;
        this.timeZone=timeZone;
        int index=range.indexOf(divider);
        if (index<0)
        {
            throw new Exception("Invalid format: "+range);
        }
        this.from=range.substring(0,index);
        this.to=range.substring(index+divider.length());
    }
    
    public String toDateTime()
    {
        return this.to;
    }
    public String fromDateTime()
    {
        return this.from;
    }

    public long toDateTimeToEpochMillis()
    {
        return toEpochMillis(this.to);
    }
    public long fromDateTimeToEpochMillis()
    {
        return toEpochMillis(this.from);
    }
    private long toEpochMillis(String dateTime)
    {
        LocalDateTime ldt=LocalDateTime.parse(dateTime, this.formatter);
        if (this.timeZone!=null)
        {
            ZoneOffset zo=ZoneOffset.ofTotalSeconds(this.timeZone.getOffset(System.currentTimeMillis())/1000);
            OffsetDateTime odt=OffsetDateTime.of(ldt, zo);
            odt.toInstant().toEpochMilli();
        }
        return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    public String range()
    {
        return this.from+divider+this.to;
        
    }
}
