package no.home.automation.ws;

import java.lang.reflect.Type;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalTimeTypeConverter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime>
{
	private DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");

	@Override
	public JsonElement serialize(LocalTime src, Type srcType, JsonSerializationContext context)
	{
		return new JsonPrimitive(src.toString(fmt));
	}

	@Override
	public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		return fmt.parseLocalTime(json.getAsString());
	}
}
