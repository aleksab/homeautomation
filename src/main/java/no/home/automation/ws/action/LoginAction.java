package no.home.automation.ws.action;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.LoginRequest;
import no.home.automation.model.LoginResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginAction extends DefaultHandler<LoginRequest, LoginResponse>
{	
	public LoginAction(boolean mustBeAuthenticated)
	{
		super(mustBeAuthenticated);
	}

	public LoginResponse doHandle(LoginRequest request)
	{
		if (request.getUsername().equalsIgnoreCase("test") && request.getPassword().equalsIgnoreCase("tester"))
		{
			logger.info("User is authenticated: {}", request);

			originalRequest.session().attribute("authenticated", true);
			originalRequest.session().attribute("userId", 123);
			return new LoginResponse(123);
		}

		return new LoginResponse();
	}

	public LoginRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, LoginRequest.class);
	}
}
