package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.InputComponent;
import org.nova.html.bootstrap4.InputGroup;
import org.nova.html.bootstrap4.Modal;
import org.nova.html.bootstrap4.ModalBody;
import org.nova.html.bootstrap4.ModalCancelButton;
import org.nova.html.bootstrap4.ModalContent;
import org.nova.html.bootstrap4.ModalDialog;
import org.nova.html.bootstrap4.ModalFooter;
import org.nova.html.bootstrap4.ModalHeader;
import org.nova.html.bootstrap4.SubmitButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.elements.Composer;
import org.nova.html.tags.form_post;

public class InputDialog extends ModalDocument
{
    public InputDialog(boolean centered,String title,String prepend,InputComponent<?> input,String action,String buttonLabel)
    {
        super(centered);
        this.header().addInner(title).addInner(new ModalCancelButton("&#10060;"));
        form_post form=this.body().returnAddInner(new form_post()).action(action);
        InputGroup group=form.returnAddInner(new InputGroup());
        if (prepend!=null)
        {
            group.prepend(prepend);
        }
        input.autofocus();
        group.addInner(input);
        group.append(new SubmitButton(buttonLabel).mr(2).color(StyleColor.success).form(form.id()));
    }
    public InputDialog(boolean centered,String title,String prepend,InputComponent<?> input,String action)
    {
        this(centered,title,prepend,input,action,"Save");
    }
    public InputDialog(String title,String prepend,InputComponent<?> input,String action)
    {
        this(false,title,prepend,input,action);
    }
    public InputDialog(String title,InputComponent<?> input,String action)
    {
        this(false,title,null,input,action);
    }
    public InputDialog(boolean centered,String title,InputComponent<?> input,String action)
    {
        this(centered,title,null,input,action);
    }
}
