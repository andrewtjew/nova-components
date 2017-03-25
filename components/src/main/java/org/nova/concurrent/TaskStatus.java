package org.nova.concurrent;

public enum TaskStatus
{
		CREATED,	// Created, but not ready 
		READY,		// ready to run, but not scheduled
		WAITING,    // scheduled to run, but waiting for resources
		EXECUTING,	
		COMPLETED,	 
}
