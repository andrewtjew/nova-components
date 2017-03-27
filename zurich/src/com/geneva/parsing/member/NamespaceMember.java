package com.geneva.parsing.member;

import java.util.List;
import com.geneva.parsing.Namespace;

public class NamespaceMember extends Member
{
	final private Namespace namespace;
	final private List<Member> members;
	public NamespaceMember(Namespace namespace,List<Member> members)
	{
		this.namespace=namespace;
		this.members=members;
	}

	public Namespace getNamespace()
	{
		return namespace;
	}

	public List<Member> getMembers()
	{
		return members;
	}
}
