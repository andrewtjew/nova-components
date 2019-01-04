package org.nova.html.bootstrap4;

public class InputGroupText extends Component<InputGroupText>
{
    public InputGroupText(String text)
    {
        super("span","input-group-text");
        addInner(text);
    }
    public InputGroupText()
    {
        super("div","input-group-text");
    }
}
