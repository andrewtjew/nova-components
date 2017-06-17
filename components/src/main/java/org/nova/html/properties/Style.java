package org.nova.html.properties;

public class Style
{
    final private StringBuilder sb;
    public Style()
    {
        this.sb=new StringBuilder();
    }
    public Style width(Size size)
    {
        sb.append("width:"+size+";");
        return this;
    }
    public Style text(String text)
    {
        sb.append(text);
        return this;
    }
    public Style height(Size size)
    {
        sb.append("height:"+size+";");
        return this;
    }
    public Style color(Color color)
    {
        sb.append("color:"+color+";");
        return this;
    }
    public Style background_color(Color color)
    {
        sb.append("background-color:"+color+";");
        return this;
    }
    public Style border(Size size,BorderStyleRect borderStyle,ColorRect color)
    {
        sb.append("border:"+size+" "+borderStyle+" "+color+";");
        return this;
    }
    public Style border(Size size,BorderStyleRect borderStyle)
    {
        sb.append("border:"+size+" "+borderStyle+";");
        return this;
    }

    public Style border(Size size)
    {
        sb.append("border:"+size+";");
        return this;
    }

    public Style margin(Size size)
    {
        sb.append("margin:"+size+";");
        return this;
    }
    public Style margin_auto(Size size)
    {
        sb.append("margin:"+size+" auto;");
        return this;
    }
    public Style margin_auto()
    {
        sb.append("margin:0 auto;");
        return this;
    }
    public Style margin_top(Size size)
    {
        sb.append("margin-top:"+size+";");
        return this;
    }
    public Style margin_right(Size size)
    {
        sb.append("margin-right:"+size+";");
        return this;
    }
    public Style margin_bottom(Size size)
    {
        sb.append("margin-bottom:"+size+";");
        return this;
    }
    public Style margin_left(Size size)
    {
        sb.append("margin-left:"+size+";");
        return this;
    }
    public Style margin(Size top,Size right,Size bottom,Size left)
    {
        sb.append("margin:"+top+" "+right+" "+bottom+" "+left+";");
        return this;
    }
    public Style padding(Size size)
    {
        sb.append("padding:"+size+";");
        return this;
    }
    public Style padding_top(Size size)
    {
        sb.append("padding-top:"+size+";");
        return this;
    }
    public Style padding_right(Size size)
    {
        sb.append("padding-right:"+size+";");
        return this;
    }
    public Style padding_bottom(Size size)
    {
        sb.append("padding-bottom:"+size+";");
        return this;
    }
    public Style padding_left(Size size)
    {
        sb.append("padding-left:"+size+";");
        return this;
    }
    public Style padding(Size top,Size right,Size bottom,Size left)
    {
        sb.append("padding:"+top+" "+right+" "+bottom+" "+left+";");
        return this;
    }

    public Style display(display value)
    {
        sb.append("display:"+value.toString()+";");
        return this;
    }
    public Style position(position value)
    {
        sb.append("position:"+value+";");
        return this;
    }
    public Style float_(float_ value)
    {
        sb.append("float:"+value+";");
        return this;
    }
    public Style vertical_align(vertical_align value)
    {
        sb.append("vertical-align:"+value+";");
        return this;
    }
    public Style vertical_align(Size value)
    {
        sb.append("vertical-align:"+value+";");
        return this;
    }
    public Style text_decoration(text_decoration value)
    {
        sb.append("text-decoration:"+value+";");
        return this;
    }
    public Style text_align(text_align value)
    {
        sb.append("text-align:"+value+";");
        return this;
    }
    public Style z_index(int value)
    {
        sb.append("z-index:"+value+";");
        return this;
    }
    public Style top(Size value)
    {
        sb.append("top:"+value+";");
        return this;
    }
    public Style left(Size value)
    {
        sb.append("left:"+value+";");
        return this;
    }
    public Style font_weight(font_weight value)
    {
        sb.append("font-size:"+value+";");
        return this;
    }
    public Style font_size(Size value)
    {
        sb.append("font-size:"+value+";");
        return this;
    }
    
    @Override
    public String toString()
    {
        return this.sb.toString();
    }
}
