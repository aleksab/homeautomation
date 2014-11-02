package no.home.automation.ws.action;

import java.util.LinkedList;
import java.util.List;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.Device;
import no.home.automation.model.ListDeviceRequest;
import no.home.automation.model.ListDeviceResponse;
import no.home.automation.rfxcom.messages.RFXComBaseMessage;
import no.home.automation.rfxcom.messages.RFXComBaseMessage.PacketType;
import no.home.automation.rfxcom.messages.RFXComLighting2Message;
import no.home.automation.service.RfxcomBus;
import no.home.automation.service.RfxcomEventListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ListDevicesAction extends DefaultHandler<ListDeviceRequest, ListDeviceResponse> implements RfxcomEventListener
{
	private RfxcomBus		bus;
	private List<Device>	devices	= null;

	public ListDevicesAction(boolean mustBeAuthenticated, RfxcomBus bus)
	{
		super(mustBeAuthenticated);
		this.bus = bus;
	}

	@Override
	public ListDeviceResponse doHandle(ListDeviceRequest request)
	{
		return new ListDeviceResponse(getActiveDevices());
	}

	@Override
	public ListDeviceRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, ListDeviceRequest.class);
	}

	public void packetReceived(RFXComBaseMessage message)
	{
		logger.debug("Package received:\n" + message.toString());

		if (message.packetType == PacketType.LIGHTING2)
		{
			RFXComLighting2Message lightMessage = (RFXComLighting2Message) message;

			logger.info("sensor: " + lightMessage.sensorId);
			logger.info("unitcode: " + lightMessage.unitcode);

			Device device = new Device(lightMessage.sensorId, lightMessage.unitcode, "Light Sensor");
			if (!devices.contains(device))
				devices.add(device);
		}
		else
		{
			logger.info("Unknown type: " + message.packetType);
			devices.add(new Device(0, 0, "Unknown Sensor (" + message.packetType + ")"));
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
