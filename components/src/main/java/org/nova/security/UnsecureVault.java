package org.nova.security;

import java.util.HashMap;

public class UnsecureVault extends Vault 
{
    private HashMap<String,String> map;
    
    public UnsecureVault()
    {
        this.map=new HashMap<>();
    }
    
    public void put(String key,String value)
    {
        this.map.put(key, value);
    }
    
    @Override
    public String get(String key) throws Exception
    {
        return this.map.get(key);
    }

}
