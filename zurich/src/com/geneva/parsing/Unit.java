package com.geneva.parsing;

import java.util.List;

import com.geneva.parsing.member.Member;

// A unit is what is in a file
public class Unit
{
	final private List<Member> members;
	public Unit(List<Member> members)
	{
		this.members=members;
	}
	public List<Member> getMembers()
	{
		return members;
	}
	
}
