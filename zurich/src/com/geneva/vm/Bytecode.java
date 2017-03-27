package com.geneva.vm;

public enum Bytecode 
{
	add_int((byte)0);
	
	private byte value;
	private Bytecode(byte value)
	{
		this.value=value;
	}
	public byte getValue()
	{
		return this.value;
	}
}
