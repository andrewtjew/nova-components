package org.nova.http.server;

public class AbnormalException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3203473253582953667L;
	
	final private Abnormal abnormal;
	AbnormalException(Abnormal abnormal,Throwable cause)
	{
		super(abnormal.toString(),cause);
		this.abnormal=abnormal;
	}
	AbnormalException(Abnormal abnormal)
	{
	    super(abnormal.toString());
		this.abnormal=abnormal;
	}
	public Abnormal getAbnormal()
	{
		return abnormal;
	}
	
}
