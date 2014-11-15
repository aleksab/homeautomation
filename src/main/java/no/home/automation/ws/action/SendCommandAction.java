package no.home.automation.ws.action;

import no.home.automation.dao.DeviceRowMapper;
import no.home.automation.model.DefaultHandler;
import no.home.automation.model.DefaultReponse;
import no.home.automation.model.Device;
import no.home.automation.model.SendCommandRequest;
import no.home.automation.model.SendCommandRequest.TYPE;
import no.home.automation.service.RfxcomBus;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SendCommandAction extends DefaultHandler<SendCommandRequest, DefaultReponse>
{
	private RfxcomBus		bus;
	private JdbcTemplate	jdbcTemplate;

	public SendCommandAction(boolean mustBeAuthenticated, RfxcomBus bus, JdbcTemplate jdbcTemplate)
	{
		super(mustBeAuthenticated);
		this.bus = bus;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public DefaultReponse doHandle(SendCommandRequest request)
	{
		Device device = findDevice(request.getSensorId(), request.getUnitCode());
		if (device == null)
			throw new IllegalArgumentException("Not a valid device");

		if (request.getType() == TYPE.ON)
			bus.sendLightOnCommand(device);
		else if (request.getType() == TYPE.OFF)
			bus.sendLightOffCommand(device);
		else if (request.getType() == TYPE.DIM)
			bus.sendLightDimCommand(device, request.getDimLevel());

		return new DefaultReponse();
	}

	@Override
	public SendCommandRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, SendCommandRequest.class);
	}

	Device findDevice(int sensorId, int unitCode)
	{
		try
		{
			return jdbcTemplate.queryForObject("SELECT * FROM device WHERE SensorId=? and UnitCode=?", new DeviceRowMapper(), sensorId, unitCode);
		}
		catch (Exception ex)
		{
			logger.error("Could not find device", ex);
			return null;
		}
	}
}
