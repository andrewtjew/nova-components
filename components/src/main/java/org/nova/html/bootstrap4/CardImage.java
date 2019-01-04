package org.nova.html.bootstrap4;

public class CardImage extends StyleComponent<CardImage>
{
    public CardImage(String src,String alt)
    {
        super("img","card-img-top",true);
        attr("src",src);
        attr("alt",alt);
    }
}
