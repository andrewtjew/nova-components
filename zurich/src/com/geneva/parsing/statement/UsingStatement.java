package com.geneva.parsing.statement;

import java.util.List;
import com.geneva.parsing.NameTypeExpression;

public class UsingStatement extends MultiStateme
{
	final private List<NameTypeExpression> nameTypeExpressions;
	
	public UsingStatement(List<NameTypeExpression> nameTypeExpressions,List<Statement> statements)
    {
	    super(statements);
	    this.nameTypeExpressions=nameTypeExpressions;
    }

	public List<NameTypeExpression> getNameTypeExpressions()
	{
		return nameTypeExpressions;
	}
	
}
