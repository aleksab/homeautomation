package no.home.automation.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class DefaultHandler<T extends RequestValidator, V> implements Route
{
	protected static final Logger	logger	= LoggerFactory.getLogger("fileLogger");

	protected Request				originalRequest;
	private boolean					mustBeAuthenticated;

	public DefaultHandler(boolean mustBeAuthenticated)
	{
		this.mustBeAuthenticated = mustBeAuthenticated;
	}

	@Override
	public final Object handle(Request request, Response response)
	{
		T incomingRequest = null;
		this.originalRequest = request;

		try
		{
			logger.info("Incoming request: {}", request.body());
			incomingRequest = translate(request.body());

			if (incomingRequest == null)
				throw new NullPointerException("Could not parse request: " + request);

			logger.info("Translated request: {}", incomingRequest);
		}
		catch (Exception ex)
		{
			logger.error("Could not translate request", ex);
			return "Could not parse incoming data. Error message: " + ex.getMessage();
		}

		try
		{
			incomingRequest.validateRequest();
		}
		catch (IllegalArgumentException ex)
		{
			logger.error("Could not validate request", ex);
			return "Could not validate request. Error message: " + ex.getMessage();
		}

		if (mustBeAuthenticated && !isAuthenticated())
		{
			return "This action is not allowed!";
		}

		try
		{
			response.type("application/json");
			return doHandle(incomingRequest);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "Internal error: " + ex.getMessage();
		}
	}

	public abstract V doHandle(T request);

	public abstract T translate(String json);

	private boolean isAuthenticated()
	{
		if (originalRequest.session().isNew() || StringUtils.isEmpty(originalRequest.session().attribute("authenticated")))
			return false;

		if (Boolean.valueOf(originalRequest.session().attribute("authenticated")))
		{
			return true;
		}

		return false;
	}
}
