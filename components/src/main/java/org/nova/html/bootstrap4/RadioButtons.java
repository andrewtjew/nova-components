package org.nova.html.bootstrap4;

import java.util.ArrayList;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.input_radio;
import org.nova.html.tags.label;

public class RadioButtons extends Element
{
    final private ArrayList<String> options;
    final private String name;
    private boolean inline;
    
    public RadioButtons(String name)
    {
        this.name=name;
        this.options=new ArrayList<>();
    }
    
    public RadioButtons inline(boolean value)
    {
        this.inline=value;
        return this;
    }
    
    public RadioButtons add(String option)
    {
        this.options.add(option);
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        for (String option:this.options)
        {
            div div=new div().class_("radio");
            label label=div.returnAddInner(new label());
            input_radio radio=label.returnAddInner(new input_radio());
            radio.name(this.name).addInner(option);
            composer.render(div);
        }
        
    }
    
   
    
    
}
