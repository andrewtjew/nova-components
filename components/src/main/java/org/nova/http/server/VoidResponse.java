package org.nova.http.server;

import org.eclipse.jetty.http.HttpStatus;

public class VoidResponse extends Response<Void>
{
    public VoidResponse()
    {
        this(HttpStatus.OK_200);
    }

    public VoidResponse(int statusCode)
    {
        super(statusCode);
    }

}
