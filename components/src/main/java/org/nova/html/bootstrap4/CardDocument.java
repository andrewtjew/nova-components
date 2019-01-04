package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;

public class CardDocument extends Card
{
    private CardHeader header;
    private CardFooter footer;
    private CardBody body;
    
    public CardHeader header()
    {
        if (this.header==null)
        {
            this.header=new CardHeader();
        }
        return this.header;
    }
    public CardFooter footer()
    {
        if (this.footer==null)
        {
            this.footer=new CardFooter();
        }
        return this.footer;
    }
    public CardBody body()
    {
        if (this.body==null)
        {
            this.body=new CardBody();
        }
        return this.body;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (this.header!=null)
        {
            this.addInner(this.header);
        }
        if (this.body!=null)
        {
            this.addInner(this.body);
        }
        if (this.footer!=null)
        {
            this.addInner(this.footer);
        }
        super.compose(composer);
    }
    
}
