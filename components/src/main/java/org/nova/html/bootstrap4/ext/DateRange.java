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
    final private String from;
    final private String to;
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
        this.from=range.substring(0,index);
        this.to=range.substring(index+divider.length());
        this.divider=divider;
    }
    
    public String toDate()
    {
        return this.to;
    }
    public String fromDate()
    {
        return this.from;
    }

    public long toDateToEpochMillis()
    {
        return toEpochMillis(this.to,0);
    }
    public long fromDateToEpochMillis()
    {
        return toEpochMillis(this.from,0);
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
            odt.toInstant().toEpochMilli();

        }
        return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    public long dateAfterToEpochMilli(long days)
    {
        return toEpochMillis(this.to,days);
    }
    
    public String range()
    {
        return this.from+divider+this.to;
        
    }
}
