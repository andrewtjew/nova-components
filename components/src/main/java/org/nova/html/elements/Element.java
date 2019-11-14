package org.nova.html.elements;

public abstract class Element
{
    abstract public void compose(Composer composer) throws Throwable;

    @Override
    public String toString()
    {
        try
        {
            StringComposer composer=new StringComposer();
            compose(composer);
            return composer.getStringBuilder().toString();
        }
        catch(Throwable t)
        {
            throw new RuntimeException(t);
        }
   }

    static public String HREF_LOCAL_DIRECTORY=null;
    
    
}