package com.geneva.parsing.statement;

import java.util.List;
import com.geneva.parsing.MutableNameType;
import com.geneva.parsing.Node;

public class CatchStatement extends MultiStateme
{
	final private List<MutableNameType> mutableNameTypes;

	public CatchStatement(List<MutableNameType> mutableNameTypes,List<Statement> statements)
    {
	    super(statements);
	    this.mutableNameTypes=mutableNameTypes;
    }

	public List<MutableNameType> getMutableNameTypes()
	{
		return mutableNameTypes;
	}

}
