package org.nova.html.bootstrap.attributes.classes;

public class progress_bar extends Class<progress_bar> 
{
	public progress_bar()
	{
		super("progress-bar");
	}
	public progress_bar success()
	{
		return add("progress-bar-success");
	}
	public progress_bar info()
	{
		return add("progress-bar-info");
	}
	public progress_bar warning()
	{
		return add("progress-bar-warning");
	}
	public progress_bar danger()
	{
		return add("progress-bar-danger");
	}
	public progress_bar stripe()
	{
		return add("progress-bar-stripe");
	}
	public progress_bar stripe_active()
	{
		return add("progress-bar-stripe active");
	}
	
}
