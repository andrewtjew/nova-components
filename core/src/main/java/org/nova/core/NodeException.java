package org.nova.core;

class NodeException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 4279539664417401913L;

    public NodeException(int index,Throwable cause)
    {
        super("MultiException node index="+index,cause);
    }
}