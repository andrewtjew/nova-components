package org.nova.html.bootstrap4;

public class InputGroupText extends StyleComponent<InputGroupText>
{
    public InputGroupText(String text)
    {
        super("span","input-group-text");
        addInner(text);
    }
    public InputGroupText()
    {
        super("span","input-group-text");
    }
}
