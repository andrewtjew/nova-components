/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Modal;
import org.nova.html.bootstrap4.ModalBody;
import org.nova.html.bootstrap4.ModalContent;
import org.nova.html.bootstrap4.ModalDialog;
import org.nova.html.bootstrap4.ModalFooter;
import org.nova.html.bootstrap4.ModalHeader;
import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.elements.Composer;
import org.nova.html.remoting.ModalOption;

public class ModalDocument extends Modal
{
    final private ModalDialog dialog;
    final private ModalContent content;
    final private ModalHeader header;
    final private ModalFooter footer;
    final private ModalBody body;
    
    public ModalDocument(boolean centered)
    {
        this(centered,null);
    }
    public ModalDocument(boolean centered,DeviceClass deviceClass)
    {
        this.dialog=returnAddInner(new ModalDialog());
        
        this.content=this.dialog.returnAddInner(new ModalContent());
        if (centered)
        {
            this.dialog.centered();
        }
        if (deviceClass!=null)
        {
            this.dialog.deviceClass(deviceClass);
        }
        this.header=new ModalHeader();
        this.footer=new ModalFooter();
        this.body=new ModalBody();
        this.content.addInner(this.header);
        this.content.addInner(this.body);
        this.content.addInner(this.footer);
    }
    public ModalDocument(DeviceClass deviceClass)
    {
        this(false,deviceClass);
    }
    
    public ModalDocument()
    {
        this(false);
    }
    
    public ModalHeader header()
    {
        return this.header;
    }
    public ModalDialog modalDialog()
    {
        return this.dialog;
    }
    public ModalFooter footer()
    {
        return this.footer;
    }
    public ModalBody body()
    {
        return this.body;
    }
    public ModalContent modalContent()
    {
        return this.content;
    }
    
    public String js_option(ModalOption option)
    {
        return "$('#"+id()+"').modal('"+option+"');";
    }
    
}
