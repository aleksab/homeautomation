package no.home.automation.ws.action;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.DefaultReponse;
import no.home.automation.model.Device;
import no.home.automation.model.SendCommandRequest;
import no.home.automation.model.SendCommandRequest.TYPE;
import no.home.automation.service.RfxcomBus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SendCommandAction extends DefaultHandler<SendCommandRequest, DefaultReponse>
{
	private RfxcomBus	bus;

	public SendCommandAction(boolean mustBeAuthenticated, RfxcomBus bus)
	{
		super(mustBeAuthenticated);
		this.bus = bus;
	}

	@Override
	public DefaultReponse doHandle(SendCommandRequest request)
	{
		boolean result = false;
		Device device = findDevice(request.getSensorId(), request.getUnitCode());

		if (request.getType() == TYPE.ON)
			result = bus.sendLightOnCommand(device);
		else if (request.getType() == TYPE.OFF)
			result = bus.sendLightOffCommand(device);
		else if (request.getType() == TYPE.DIM)
			result = bus.sendLightDimCommand(device, request.getDimLevel());
		else
			result = false;

		return new DefaultReponse(result);
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
		return new Device(sensorId, unitCode, "");
	}
}
