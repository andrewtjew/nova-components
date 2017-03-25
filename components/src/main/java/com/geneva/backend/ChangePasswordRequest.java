package com.geneva.backend;

import org.nova.annotations.Description;

public class ChangePasswordRequest
{
	@Description("User")
	public String user;

	@Description("Existing password")
	public String password;
	
	@Description("New password. Must not be the same as existing password")
	public String newPassword;
}
