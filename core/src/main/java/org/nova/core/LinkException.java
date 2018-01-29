package org.nova.core;

class LinkException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 4279539664417401913L;

    public LinkException(int index,Throwable cause)
    {
        super("MultiException link index="+index,cause);
    }
}