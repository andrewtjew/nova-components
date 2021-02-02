package org.nova.html.remote;

import org.nova.html.elements.InputElement;
import org.nova.json.ObjectMapper;

public class Input
{
    final public String id;
    final public String name;
    final public InputType inputType;
    
    public Input(String name,Object value) throws Throwable
    {
        if (value!=null)
        {
            Class<?> type=value.getClass();
            if (type.isPrimitive())
            {
                this.id=value.toString();
            }
            else if (type==String.class)
            {
                this.id=value.toString();
            }
            else
            {
                this.id=ObjectMapper.writeObjectToString(value);
            }
        }
        else
        {
            this.id=null;
        }
        this.name=name;
        this.inputType=InputType.constant;
    }
    
    public Input(InputElement<?> inputElement)
    {
        this.id=inputElement.id();
        this.name=inputElement.name();
        switch (inputElement.getInputType())
        {
            case button:
            case color:
            case date:
            case datetime_local:
            case month:
            case number:
            case password:
            case search:
            case email:
            case tel:
            case text:
            case textarea:
            case time:
            case url:
            case hidden:
            case submit:
            case range:
            case week:
            inputType=InputType.value;
            break;
            
            case checkbox:
            inputType=InputType.checked;
            break;

            case select:
            inputType=InputType.select;
            break;

            case radio:
            inputType=InputType.radio;
             break;
             
            case image:
            case reset:
            case file:
            default:
                inputType=null;
                break;
        }
    }
}
