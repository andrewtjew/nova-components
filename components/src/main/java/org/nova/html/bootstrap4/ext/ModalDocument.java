package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Modal;
import org.nova.html.bootstrap4.ModalBody;
import org.nova.html.bootstrap4.ModalContent;
import org.nova.html.bootstrap4.ModalDialog;
import org.nova.html.bootstrap4.ModalFooter;
import org.nova.html.bootstrap4.ModalHeader;
import org.nova.html.elements.Composer;

public class ModalDocument extends Modal
{
    final private ModalDialog dialog;
    final private ModalContent content;
    private ModalHeader header;
    private ModalFooter footer;
    private ModalBody body;
    
    public ModalDocument(boolean centered)
    {
        this.dialog=returnAddInner(new ModalDialog());
        
        this.content=this.dialog.returnAddInner(new ModalContent());
        if (centered)
        {
            this.dialog.centered();
        }
    }
    
    public ModalDocument()
    {
        this(false);
    }
    
    public ModalHeader header()
    {
        if (this.header==null)
        {
            this.header=new ModalHeader();
        }
        return this.header;
    }
    public ModalDialog modalDialog()
    {
        return this.dialog;
    }
    public ModalFooter footer()
    {
        if (this.footer==null)
        {
            this.footer=new ModalFooter();
        }
        return this.footer;
    }
    public ModalBody body()
    {
        if (this.body==null)
        {
            this.body=new ModalBody();
        }
        return this.body;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (this.header!=null)
        {
            this.content.addInner(this.header);
        }
        if (this.body!=null)
        {
            this.content.addInner(this.body);
        }
        if (this.footer!=null)
        {
            this.content.addInner(this.footer);
        }
        super.compose(composer);
    }
    
}
