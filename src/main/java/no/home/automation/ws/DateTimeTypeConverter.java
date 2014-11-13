package no.home.automation.ws;

import java.lang.reflect.Type;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime>
{
	private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context)
	{
		return new JsonPrimitive(src.toString(fmt));
	}

	@Override
	public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		return fmt.parseDateTime(json.getAsString());
	}
}
