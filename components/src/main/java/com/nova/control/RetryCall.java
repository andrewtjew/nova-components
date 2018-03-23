package com.nova.control;

import java.util.ArrayList;
import org.nova.core.MultiException;
import org.nova.logging.Logger;

public interface RetryCall<RETURN>
{
    RETURN call() throws Throwable;
    
    public static <RETURN> RETURN call(Class<RETURN> returnClass,int attempts,long sleep,Logger logger,RetryCall<RETURN> code) throws MultiException
    {
        ArrayList<Throwable> throwables=null;
        for (int i=0;i<attempts;i++)
        {
            try
            {
                return code.call();
            }
            catch (Throwable t)
            {
                if (logger!=null)
                {
                    logger.log(t,"Retry attempt:"+(i+1)+" out of "+attempts);
                }
                if (throwables==null)
                {
                    throwables=new ArrayList<>();
                }
                throwables.add(t);
                if (sleep>0)
                {
                    try
                    {
                        Thread.sleep(sleep);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }
        throw new MultiException(throwables);
    }
}
