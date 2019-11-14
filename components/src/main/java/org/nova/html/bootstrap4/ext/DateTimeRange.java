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
    final private String start;
    final private String end;
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
        this.start=range.substring(0,index);
        this.end=range.substring(index+divider.length());
    }
    
    public String end()
    {
        return this.end;
    }
    public String start()
    {
        return this.start;
    }

    public long endEpochMillis()
    {
        return toEpochMillis(this.end);
    }
    public long startEpochMillis()
    {
        return toEpochMillis(this.start);
    }
    private long toEpochMillis(String dateTime)
    {
        LocalDateTime ldt=LocalDateTime.parse(dateTime, this.formatter);
        if (this.timeZone!=null)
        {
            ZoneOffset zo=ZoneOffset.ofTotalSeconds(this.timeZone.getOffset(System.currentTimeMillis())/1000);
            OffsetDateTime odt=OffsetDateTime.of(ldt, zo);
            return odt.toInstant().toEpochMilli();
        }
        return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    public String range()
    {
        return this.start+divider+this.end;
        
    }
}
