package org.nova.services;

import java.util.UUID;

public class TokenGenerator
{
    public TokenGenerator()
    {
    }
    public String next()
    {
        return UUID.randomUUID().toString();
    }
}
