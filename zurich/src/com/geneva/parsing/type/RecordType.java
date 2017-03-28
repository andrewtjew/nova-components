package com.geneva.parsing.type;

import java.util.List;

import com.geneva.parsing.NameType;

public class RecordType extends Type
{
	List<NameType> nameTypes;
	
	public RecordType(List<NameType> nameTypes)
	{
		this.nameTypes=nameTypes;
	}

	public List<NameType> getNameTypes()
	{
		return nameTypes;
	}

	public void setNameTypes(List<NameType> nameTypes)
	{
		this.nameTypes = nameTypes;
	}
}
