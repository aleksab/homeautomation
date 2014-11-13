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
			result = deleteDevice(request.getDevice());
		else if (request.getType() == TYPE.ADD)
			result = createNewDevice(request.getDevice());
		else if (request.getType() == TYPE.RENAME)
			result = updateDevice(request.getDevice());

		return new UpdateDeviceResponse(request.getDevice(), result);
	}

	@Override
	public UpdateDeviceRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, UpdateDeviceRequest.class);
	}

	boolean deleteDevice(Device device)
	{
		try
		{
			jdbcTemplate.update("DELETE FROM device WHERE id=?", device.getId());
			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not delete device", ex);
			return false;
		}
	}

	boolean updateDevice(Device device)
	{
		try
		{
			jdbcTemplate.update("UPDATE device SET name=? WHERE id=?", device.getName(), device.getId());
			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not update device", ex);
			return false;
		}
	}

	boolean createNewDevice(Device device)
	{
		try
		{
			SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleInsert.withTableName("device");
			simpleInsert.setGeneratedKeyName("id");

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("sensorId", device.getSensorId());
			parameters.put("unitCode", device.getUnitCode());
			parameters.put("name", device.getName());

			int id = simpleInsert.executeAndReturnKey(parameters).intValue();
			device.setId(id);

			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not insert device", ex);
			return false;
		}
	}
}
