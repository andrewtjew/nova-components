package org.nova.html.bootstrap4;

public class Image extends StyleComponent<Image>
{
    public Image(String src,String alt,int width,int height)
    {
        super("img",null,true);
        attr("src",src);
        attr("alt",alt);
        attr("width",width);
        attr("height",height);
    }
    public Image(String src,String alt)
    {
        super("img",null,true);
        attr("src",src);
        attr("alt",alt);
    }

    public Image(String src)
    {
        this(src,null);
    }

    public Image img_thumbnail()
    {
        addClass("img-thumbnail");
        return this;
    }

    public Image img_fluid()
    {
        addClass("img-fluid");
        return this;
    }


}
