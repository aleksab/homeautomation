package no.home.automation.ws.action;

import java.util.HashMap;
import java.util.Map;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.Device;
import no.home.automation.model.UpdateDeviceRequest;
import no.home.automation.model.UpdateDeviceRequest.TYPE;
import no.home.automation.model.UpdateDeviceResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UpdateDeviceAction extends DefaultHandler<UpdateDeviceRequest, UpdateDeviceResponse>
{
	private JdbcTemplate	jdbcTemplate	= null;

	public UpdateDeviceAction(boolean mustBeAuthenticated, JdbcTemplate jdbcTemplate)
	{
		super(mustBeAuthenticated);
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public UpdateDeviceResponse doHandle(UpdateDeviceRequest request)
	{
		boolean result = false;
		if (request.getType() == TYPE.DELETE)
			result = deleteDevice(request.getSensorId(), request.getUnitCode());
		else if (request.getType() == TYPE.ADD)
			result = createNewDevice(request.getSensorId(), request.getUnitCode(), request.getName());
		else if (request.getType() == TYPE.RENAME)
			result = updateDevice(request.getSensorId(), request.getUnitCode(), request.getName());

		return new UpdateDeviceResponse(new Device(request.getSensorId(), request.getUnitCode(), request.getName(), request.getTurnOffAfter()),
				result);
	}

	@Override
	public UpdateDeviceRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, UpdateDeviceRequest.class);
	}

	boolean deleteDevice(int sensorId, int unitCode)
	{
		try
		{
			jdbcTemplate.update("DELETE FROM device WHERE sensorId=? and unitCode=?", sensorId, unitCode);
			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not update device", ex);
			return false;
		}
	}

	boolean updateDevice(int sensorId, int unitCode, String name)
	{
		try
		{
			jdbcTemplate.update("UPDATE device SET name=? WHERE sensorId=? and unitCode=?", name, sensorId, unitCode);
			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not update device", ex);
			return false;
		}
	}

	boolean createNewDevice(int sensorId, int unitCode, String name)
	{
		try
		{
			SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleInsert.withTableName("device");
			simpleInsert.setGeneratedKeyName("id");

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("sensorId", sensorId);
			parameters.put("unitCode", unitCode);
			parameters.put("name", name);

			simpleInsert.execute(parameters);
			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not insert device", ex);
			return false;
		}
	}
}
