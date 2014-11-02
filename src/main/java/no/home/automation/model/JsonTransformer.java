package no.home.automation.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ResponseTransformer;

import com.google.gson.Gson;

public class JsonTransformer implements ResponseTransformer
{
	private static final Logger	logger	= LoggerFactory.getLogger("fileLogger");

	private Gson				gson	= new Gson();

	@Override
	public String render(Object model)
	{
		logger.info("Outgoing response: {}", model);
		return gson.toJson(model);
	}
}
