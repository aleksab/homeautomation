package no.home.automation.ws.action;

import java.util.List;

import no.home.automation.dao.DeviceRowMapper;
import no.home.automation.model.DefaultHandler;
import no.home.automation.model.Device;
import no.home.automation.model.ListDeviceRequest;
import no.home.automation.model.ListDeviceResponse;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ListDevicesAction extends DefaultHandler<ListDeviceRequest, ListDeviceResponse>
{
	private JdbcTemplate	jdbcTemplate	= null;

	public ListDevicesAction(boolean mustBeAuthenticated, JdbcTemplate jdbcTemplate)
	{
		super(mustBeAuthenticated);
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ListDeviceResponse doHandle(ListDeviceRequest request)
	{
		return new ListDeviceResponse(getAllDevices());
	}

	@Override
	public ListDeviceRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, ListDeviceRequest.class);
	}

	List<Device> getAllDevices()
	{
		return jdbcTemplate.query("SELECT * FROM device", new DeviceRowMapper());
	}
}
