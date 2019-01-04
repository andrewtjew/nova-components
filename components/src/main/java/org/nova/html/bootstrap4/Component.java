package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Float_;
import org.nova.html.bootstrap4.classes.Trigger;
import org.nova.html.elements.GlobalEventTagElement;

public abstract class Component<ELEMENT extends Component<ELEMENT>> extends GlobalEventTagElement<ELEMENT> 
{
    final private String componentClass;
//    private DeviceClass deviceClass;

    public Component(String tag,String componentClass)
    {
        this(tag,componentClass,false);
    }
    
    public Component(String tag,String componentClass,boolean noEndTag)
    {
        super(tag,noEndTag);
        this.componentClass=componentClass;
        addClass(componentClass);
    }
    
    protected String getComponentClass()
    {
        return this.componentClass;
    }
    /*
    protected DeviceClass getDeviceClass()
    {
        return this.deviceClass;
    }
    */
    
    public ELEMENT addClass(Object class_,Object...fragments)
    {
        if (fragments!=null)
        {
            if (class_!=null)
            {
                StringBuilder sb=new StringBuilder(class_.toString());
                for (Object fragment:fragments)
                {
                    if (fragment!=null)
                    {
                        sb.append('-').append(fragment);
                    }
                }
                addClass(sb.toString());
            }
        }
        return (ELEMENT)this;
    }
    
    public ELEMENT col(DeviceClass deviceClass,int columns)
    {
        return addClass("col",deviceClass,columns);
    }
    public ELEMENT col(DeviceClass deviceClass)
    {
    //    this.deviceClass=deviceClass;
        return addClass("col",deviceClass);
    }
    public ELEMENT col(int columns)
    {
        return addClass("col",columns);
    }
    public ELEMENT col()
    {
        return addClass("col");
    }
    public ELEMENT float_(DeviceClass deviceClass,Float_ value)
    {
        return addClass("float",deviceClass,value);
    }
    public ELEMENT form_control()
    {
        return addClass("form-control");
    }
    public ELEMENT popoverTrigger(Trigger value)
    {
        attr("data-trigger",value);
        return (ELEMENT)this;
    }
    public ELEMENT tooltip(String title)
    {
        attr("data-toggle","tooltip");
        title(title);
        return (ELEMENT)this;
    }
    public ELEMENT deviceClass(DeviceClass deviceClass)
    {
        return addClass(this.getComponentClass(),deviceClass);
    }

}
