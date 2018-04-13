package org.nova.html.xbootstrap4;

import org.nova.html.bootstrap4.ClassBuilder;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.input_text;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.widgets.Head;
import org.nova.html.widgets.ObjectBuilder;

public class DatetimeRangePicker extends Element
{
    private final input_text input;
    private final String name;
    private boolean showDropdowns;
    private boolean showWeekNumbers;
    private boolean showISOWeekNumbers;
    private boolean singleDatePicker;
    private boolean timePicker;
    private boolean timePicker24Hour;
    private boolean timerPickerSeconds;
    private boolean autoApply;
    private boolean linkedCalendars;
    private boolean autoUpdateInput;
    private boolean alwaysShowCalendars;
    private boolean showCustomRangeLabel;

    private String buttonClasses;
    private String applyClass;
    private String cancelClass;

    private String minDate;
    private String maxDate;
    private String startDate;
    private String endDate;
    
    private DateTimeRange[] ranges;
    /*
    private boolean Long startDate;
    private boolean Long endDate;
    private boolean Long minDate;
    private boolean Long maxDate;
    */
    public DatetimeRangePicker(Head head,String name,String value,int size,String cssFilePath)
    {
        this.name=name;
        this.input=new input_text().name(name).value(value).size(size);
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(cssFilePath);
        script script=new script().src("/resources/html/xbootstrap4/datetimepicker/js/daterangepicker.js");
        script momentScript=new script().src("/resources/html/xbootstrap4/datetimepicker/js/moment.min.js");
        if (head!=null)
        {
            head.add(this.getClass().getCanonicalName(),link);
            head.add(this.getClass().getCanonicalName()+"_moment",momentScript);
            head.add(this.getClass().getCanonicalName(),script);
        }
        else
        {
            this.input.addInner(script);
            this.input.addInner(link);
        }
    }
    
    

    public DatetimeRangePicker(Head head,String name,String value,int size)
    {
        this(head,name,value,size,"/resources/html/xbootstrap4/datetimepicker/css/daterangepicker.css");
    }
    
    public DatetimeRangePicker showDropdowns(boolean value)
    {
        this.showDropdowns=value;
        return this;
    }
    public DatetimeRangePicker showWeekNumbers(boolean value)
    {
        this.showWeekNumbers=value;
        return this;
    }
    public DatetimeRangePicker showISOWeekNumbers(boolean value)
    {
        this.showISOWeekNumbers=value;
        return this;
    }
    public DatetimeRangePicker singleDatePicker(boolean value)
    {
        this.singleDatePicker=value;
        return this;
    }
    public DatetimeRangePicker timePicker(boolean value)
    {
        this.timePicker=value;
        return this;
    }
    public DatetimeRangePicker timePicker24Hour(boolean value)
    {
        this.timePicker24Hour=value;
        return this;
    }
    public DatetimeRangePicker timerPickerSeconds(boolean value)
    {
        this.timerPickerSeconds=value;
        return this;
    }
    public DatetimeRangePicker autoApply(boolean value)
    {
        this.autoApply=value;
        return this;
    }
    public DatetimeRangePicker linkedCalendars(boolean value)
    {
        this.linkedCalendars=value;
        return this;
    }
    public DatetimeRangePicker autoUpdateInput(boolean value)
    {
        this.autoUpdateInput=value;
        return this;
    }
    public DatetimeRangePicker alwaysShowCalendars(boolean value)
    {
        this.alwaysShowCalendars=value;
        return this;
    }
    public DatetimeRangePicker showCustomRangeLabel(boolean value)
    {
        this.showCustomRangeLabel=value;
        return this;
    }
    public DatetimeRangePicker buttonClasses(String value)
    {
        this.buttonClasses=value;
        return this;
    }
    public DatetimeRangePicker applyClass(String value)
    {
        this.applyClass=value;
        return this;
    }
    public DatetimeRangePicker cancelClass(String value)
    {
        this.cancelClass=value;
        return this;
    }
    public DatetimeRangePicker startDate(String value)
    {
        this.startDate=value;
        return this;
    }
    public DatetimeRangePicker endDate(String value)
    {
        this.endDate=value;
        return this;
    }
    public DatetimeRangePicker minDate(String value)
    {
        this.minDate=value;
        return this;
    }
    public DatetimeRangePicker maxDate(String value)
    {
        this.maxDate=value;
        return this;
    }
    public DatetimeRangePicker ranges(DateTimeRange[] ranges)
    {
        this.ranges=ranges;
        return this;
    }

    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder();
        cb.add("form-control");
        cb.applyTo(this.input);
        composer.render(this.input);
        
        
        ObjectBuilder ob=new ObjectBuilder();
        ob.addIfNotNull("showDropdowns",this.showDropdowns);
        ob.addIfNotNull("showWeekNumbers",this.showWeekNumbers);
        ob.addIfNotNull("showISOWeekNumbers",this.showISOWeekNumbers);
        ob.addIfNotNull("singleDatePicker",this.singleDatePicker);
        ob.addIfNotNull("timePicker",this.timePicker);
        ob.addIfNotNull("timePicker24Hour",this.timePicker24Hour);
        ob.addIfNotNull("timerPickerSeconds",this.timerPickerSeconds);
        ob.addIfNotNull("autoApply",this.autoApply);
        ob.addIfNotNull("linkedCalendars",this.linkedCalendars);
        ob.addIfNotNull("autoUpdateInput",this.autoUpdateInput);
        ob.addIfNotNull("alwaysShowCalendars",this.alwaysShowCalendars);
        ob.addIfNotNull("showCustomRangeLabel",this.showCustomRangeLabel);
        ob.addIfNotNull("buttonClasses",this.buttonClasses);
        ob.addIfNotNull("applyClass",this.applyClass);
        ob.addIfNotNull("cancelClass",this.cancelClass);
        ob.addIfNotNull("startDate",this.startDate);
        ob.addIfNotNull("endDate",this.endDate);
        ob.addIfNotNull("minDate",this.minDate);
        ob.addIfNotNull("maxDate",this.maxDate);
        if (this.ranges!=null)
        {
            ob.begin("ranges");
            for (DateTimeRange range:this.ranges)
            {
                ob.addRawString(range.label, "[\""+range.from+"\",\""+range.to+"\"]");
            }
            ob.end();
        }
        StringBuilder sb=new StringBuilder();
        sb.append("$(function() {$('input[name=\""+this.name+"\"]').daterangepicker("+ob+");});");
        
        
        script script=new script().addInner(sb.toString());
        composer.getStringBuilder().append(script.toString());
    }

}
