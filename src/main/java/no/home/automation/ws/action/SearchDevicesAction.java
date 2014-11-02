package no.home.automation.ws.action;

import java.util.LinkedList;
import java.util.List;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.Device;
import no.home.automation.model.SearchDeviceRequest;
import no.home.automation.model.SearchDeviceResponse;
import no.home.automation.rfxcom.messages.RFXComBaseMessage;
import no.home.automation.rfxcom.messages.RFXComBaseMessage.PacketType;
import no.home.automation.rfxcom.messages.RFXComLighting2Message;
import no.home.automation.service.RfxcomBus;
import no.home.automation.service.RfxcomEventListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchDevicesAction extends DefaultHandler<SearchDeviceRequest, SearchDeviceResponse> implements RfxcomEventListener
{
	private RfxcomBus		bus;
	private List<Device>	devices	= null;

	public SearchDevicesAction(boolean mustBeAuthenticated, RfxcomBus bus)
	{
		super(mustBeAuthenticated);
		this.bus = bus;
	}

	@Override
	public SearchDeviceResponse doHandle(SearchDeviceRequest request)
	{
		return new SearchDeviceResponse(getActiveDevices());
	}

	@Override
	public SearchDeviceRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, SearchDeviceRequest.class);
	}

	public void packetReceived(RFXComBaseMessage message)
	{
		logger.debug("Package received:\n" + message.toString());

		if (message.packetType == PacketType.LIGHTING2)
		{
			RFXComLighting2Message lightMessage = (RFXComLighting2Message) message;

			logger.info("sensor: " + lightMessage.sensorId);
			logger.info("unitcode: " + lightMessage.unitcode);

			Device device = new Device(lightMessage.sensorId, lightMessage.unitcode, "Light Sensor", 0);
			if (!devices.contains(device))
				devices.add(device);
		}
		else
		{
			logger.info("Unknown type: " + message.packetType);
			devices.add(new Device(0, 0, "Unknown Sensor (" + message.packetType + ")", 0));
		}
	}

	List<Device> getActiveDevices()
	{
		devices = new LinkedList<>();
		bus.addEventListener(this);

		try
		{
			Thread.sleep(5 * 1000);
		}
		catch (InterruptedException ex)
		{
			logger.warn("Could not sleep", ex);
		}

		bus.removeEventListener(this);

		return devices;
	}
}
