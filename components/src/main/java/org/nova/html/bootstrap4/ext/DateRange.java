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

public class DateRange
{
    final private DateTimeFormatter formatter;
    final private String start;
    final private String end;
    final private TimeZone timeZone;
    final private String divider;
    
    public DateRange(String range,TimeZone timeZone,DateTimeFormatter formatter) throws Throwable
    {
        this(range,timeZone,formatter," - ");
    }
    public DateRange(String range,TimeZone timeZone,DateTimeFormatter formatter,String divider) throws Throwable
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
        this.divider=divider;
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
        return toEpochMillis(this.end,0);
    }
    public long startEpochMillis()
    {
        return toEpochMillis(this.start,0);
    }
    private long toEpochMillis(String date,long days)
    {
        LocalDate ld=LocalDate.parse(date, this.formatter);
        LocalDateTime ldt=LocalDateTime.of(ld,LocalTime.MIN);
        ldt=ldt.plusDays(days);
        if (this.timeZone!=null)
        {
            ZoneOffset zo=ZoneOffset.ofTotalSeconds(this.timeZone.getOffset(System.currentTimeMillis())/1000);
            OffsetDateTime odt=OffsetDateTime.of(ldt,zo);
            return odt.toInstant().toEpochMilli();

        }
        return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    public long dateAfterEndEpochMillis(long days)
    {
        return toEpochMillis(this.end,days);
    }
    
    public String range()
    {
        return this.start+divider+this.end;
        
    }
}
