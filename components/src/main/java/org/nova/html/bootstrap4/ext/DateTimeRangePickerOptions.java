package org.nova.html.bootstrap4.ext;

public class DateTimeRangePickerOptions
{
    static public enum OpenPosition
    {
        right,
        left,
        center,
    }
    
    static public enum DropPosition
    {
        up,
        down,
    }
    
    public Boolean showDropdowns;
    public Boolean showWeekNumbers;
    public Boolean showISOWeekNumbers;
    public Boolean singleDatePicker;
    public Boolean timePicker;
    public Boolean timePicker24Hour;
    public Boolean timePickerSeconds;
    public Boolean autoApply;
    public Boolean linkedCalendars;
    public Boolean autoUpdateInput;
    public Boolean alwaysShowCalendars;
    public Boolean showCustomRangeLabel;
    public Integer timePickerIncrement;
    public Integer maxSpan;
    public OpenPosition opens;
    public DropPosition drops;

    public String buttonClasses;
    public String applyClass;
    public String cancelClass;

    public String minDate;
    public String maxDate;
    public String startDate;
    public String endDate;
    public DateTimeRangeLocale locale;
    public DateTimeRange[] ranges;
}
