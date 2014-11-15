package no.home.automation.model;

public class LoginResponse extends DefaultReponse
{
	private int	userId;

	public LoginResponse()
	{
		this.userId = 0;
	}

	public LoginResponse(int userId)
	{
		this.userId = userId;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}
}
