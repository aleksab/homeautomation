package no.home.automation.model;

import org.apache.commons.lang.StringUtils;

public class LoginRequest extends RequestValidator
{
	private String	username;
	private String	password;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{
		if (StringUtils.isEmpty(username))
			throw new IllegalArgumentException("Username is missing");
		if (StringUtils.isEmpty(password))
			throw new IllegalArgumentException("Username is missing");
	}
}
