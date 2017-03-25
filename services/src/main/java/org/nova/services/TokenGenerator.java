package org.nova.services;

import java.util.UUID;

public class TokenGenerator
{
    public TokenGenerator()
    {
    }
    public String generate()
    {
        return UUID.randomUUID().toString();
    }
}
