package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.BackgroundColor;
import org.nova.html.bootstrap4.classes.Responsiveness;
import org.nova.html.bootstrap4.classes.TextColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.widgets.Content;
import org.nova.html.widgets.Text;

public class Column extends Element 
{
    final private Content content;
    private Responsiveness responsiveness;
    private Integer number;
    private AlignSelf align;
    private BackgroundColor backgroundColor;
    private TextColor textColor;
    

    public Column()
    {
        this.content=new Content();
    }
    
    public Column responsiveness(Responsiveness value)
    {
        this.responsiveness=value;
        return this;
    }
    
    public Column number(Integer value)
    {
        this.number=value;
        return this;
    }
    
    public Column align(AlignSelf value)
    {
        this.align=value;
        return this;
    }
    
    public Column backgroundColor(BackgroundColor value)
    {
        this.backgroundColor=value;
        return this;
    }

    public Column textColor(TextColor value)
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
        ClassBuilder class_=new ClassBuilder("col",this.responsiveness,this.number);
        class_.add(this.backgroundColor);
        class_.add(this.textColor);

        div div=new div();
	    class_.addTo(div);
	    div.addInner(this.content);
	    composer.render(div);
    }
}
