package org.nova.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeUtils
{
    //ZoneOffset at a zone is not always constant due to DST changes.
    static public ZoneOffset getNowZoneOffset(ZoneId zoneId)
    {
        Instant now=Instant.now();
        LocalDateTime local=LocalDateTime.ofInstant(now,zoneId);
        ZoneOffset offset=zoneId.getRules().getOffset(local);
        return offset;
    }
    public static ZoneId UTCZoneId=ZoneId.of("UTC");
    public static String toSystemDateTimeString(long millis)
    {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), UTCZoneId).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    
}
