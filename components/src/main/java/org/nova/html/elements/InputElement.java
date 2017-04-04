package org.nova.html.elements;

public class InputElement<ELEMENT extends InputElement<ELEMENT>> extends GlobalEventTagElement<ELEMENT>
{
    InputElement()
    {
        super("input");
    }

    public ELEMENT autofocus()
    {
        return attr("autofocus");
    }
    public ELEMENT disabled()
    {
        return attr("disabled");
    }
    @SuppressWarnings("unchecked")
    public ELEMENT disabled(boolean disabled)
    {
        if (disabled)
        {
            attr("disabled");
        }
        return (ELEMENT)this;
    }
    public ELEMENT form(String form_id)
    {
        return attr("form",form_id);
    }
    public ELEMENT name(String text)
    {
        return attr("name",text);
    }
    public ELEMENT readonly()
    {
        return attr("readonly");
    }
    public ELEMENT readonly(boolean readonly)
    {
        if (readonly)
        {
            attr("readonly");
        }
        return (ELEMENT)this;
    }


    /*
    public input accept(String value) //file
    {
        return attr("accept",value);
    }
    public input alt(String text) //image
    {
        return attr("alt",text);
    }
    public input autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("accept",autocomplete);
    }
    public input checked() //checkbox or radio
    {
        return attr("checked");
    }
    public input checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    
    
    public input dirname(String value) //text
    {
        return attr("dirname",value);
    }
    public input formaction(String URL) //submit, image
    {
        return attr("formaction",URL);
    }
    public input formenctype(String value) //submit, image
    {
        return attr("formenctype",value);
    }
    public input formmethod(String value) //submit, image
    {
        return attr("formmethod",value);
    }
    public input formnovalidate()  //submit
    {
        return attr("formnovalidate");
    }
    public input formnovalidate(boolean formnovalidate)
    {
        if (formnovalidate)
        {
            attr("formnovalidate");
        }
        return this;
    }
    public input formtarget(boolean formnovalidate)
    {
        if (formnovalidate)
        {
            attr("formnovalidate");
        }
        return this;
    }
    public input height(String pixels) //image
    {
        return attr("height",pixels);
    }
    public input max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input max(long number)
    {
        return attr("max",number);
    }
    public input maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public input min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input min(long number)
    {
        return attr("min",number);
    }
    public input multiple() //file
    {
        return attr("multiple");
    }
    public input multiple(boolean multiple) //file, email
    {
        if (multiple)
        {
            attr("multiple");
        }
        return this;
    }
    public input pattern(String regex) text, date, search, url, tel, email, and password.
    {
        return attr("pattern",regex);
    }
    public ELEMENT placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input src(String URL) //image
    {
        return attr("src",URL);
    }
    public input step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input width(String pixels) //image
    {
        return attr("width",pixels);
    }
    public input required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    */

}
