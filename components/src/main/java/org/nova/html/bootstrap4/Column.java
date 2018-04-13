package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.Responsiveness;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.widgets.Content;
import org.nova.html.widgets.Text;

public class Column extends Element 
{
    final private Content content;
    private Responsiveness responsiveness;
    private Integer responsivenessNumber;
    private AlignSelf align;
    private ThemeColor backgroundColor;
    private ThemeColor textColor;
    private Integer offset;

    public Column()
    {
        this.content=new Content();
    }
    
    public Column responsiveness(Responsiveness responsiveness,Integer number)
    {
        this.responsiveness=responsiveness;
        this.responsivenessNumber=number;
        return this;
    }
    
    public Column align(AlignSelf value)
    {
        this.align=value;
        return this;
    }
    
    public Column backgroundColor(ThemeColor value)
    {
        this.backgroundColor=value;
        return this;
    }

    public Column offset(Integer value)
    {
        this.offset=value;
        return this;
    }

    public Column textColor(ThemeColor value)
    {
        this.textColor=value;
        return this;
    }
    
    public Column add(Element element)
    {
        this.content.addInner(element);
        return this;
    }
    public Column add(Object object)
    {
        this.content.addInner(new Text(object));
        return this;
    }
	@Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("col");
        cb.addFragmentsIf(this.responsiveness!=null,this.responsiveness,this.responsivenessNumber);
        cb.addIf(this.offset!=null&&this.responsiveness!=null,"offset",this.responsiveness,this.offset);
        cb.addIf(this.backgroundColor!=null,"bg",this.backgroundColor);
        cb.addIf(this.textColor!=null,"text",this.textColor);

        div div=new div();
	    cb.applyTo(div);
	    div.addInner(this.content);
	    composer.render(div);
    }
}
