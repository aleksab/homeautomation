package no.home.automation.model;

import no.home.automation.ws.DateTimeTypeConverter;
import no.home.automation.ws.LocalTimeTypeConverter;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ResponseTransformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTransformer implements ResponseTransformer
{
	private static final Logger	logger	= LoggerFactory.getLogger("fileLogger");

	private Gson				gson	= new Gson();

	public JsonTransformer()
	{
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeConverter());
		gson = gsonBuilder.create();
	}

	@Override
	public String render(Object model)
	{
		logger.info("Outgoing response: {}", model);
		return gson.toJson(model);
	}
}
