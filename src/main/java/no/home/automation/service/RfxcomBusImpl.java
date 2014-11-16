package no.home.automation.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import no.home.automation.model.Device;
import no.home.automation.rfxcom.RFXComEventListener;
import no.home.automation.rfxcom.RFXComMessageReceivedEvent;
import no.home.automation.rfxcom.RFXComSerialConnector;
import no.home.automation.rfxcom.messages.RFXComBaseMessage;
import no.home.automation.rfxcom.messages.RFXComLighting2Message;
import no.home.automation.rfxcom.messages.RFXComMessageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RfxcomBusImpl implements RfxcomBus, RFXComEventListener
{
	private static final Logger			logger			= LoggerFactory.getLogger("fileLogger");

	private List<RfxcomEventListener>	listenerList	= null;
	private RFXComSerialConnector		connector		= null;

	public RfxcomBusImpl()
	{
		listenerList = new LinkedList<>();
	}

	@Override
	public void addEventListener(RfxcomEventListener listener)
	{
		listenerList.add(listener);
	}

	@Override
	public void removeEventListener(RfxcomEventListener listener)
	{
		listenerList.remove(listener);
	}

	@Override
	public void packetReceived(EventObject event, byte[] data)
	{
		RFXComBaseMessage message = RFXComMessageUtils.decodePacket(data);
		logger.debug("Package received:\n" + message.toString());

		for (RfxcomEventListener listener : listenerList)
		{
			listener.packetReceived(message);
		}
	}

	public boolean sendLightOnCommand(Device device)
	{
		byte[] data = getLightOnCommand(device);
		return sendCommand(data);
	}

	public boolean sendLightOffCommand(Device device)
	{
		byte[] data = getLightOffCommand(device);
		return sendCommand(data);
	}

	public boolean sendLightDimCommand(Device device, int dimPercentage)
	{
		byte[] data = getLightDimCommand(device, dimPercentage);
		return sendCommand(data);
	}

	boolean sendCommand(byte[] data)
	{
		try
		{
			connector.sendMessage(data);
			packetReceived(new RFXComMessageReceivedEvent(this), data);
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	public void startBus(String port)
	{
		try
		{
			connector = new RFXComSerialConnector();
			connector.connect(port);
			logger.debug("Connected to RFXCom: " + port);
		}
		catch (Exception ex)
		{
			logger.error("Could not connect to RFXCom: " + port, ex);
			connector = null;
		}

		if (connector != null)
		{
			connector.addEventListener(this);

			Runtime.getRuntime().addShutdownHook(new Thread()
			{
				public void run()
				{
					shutdown();
				}
			});
		}
	}

	public void shutdown()
	{
		if (connector != null)
			connector.disconnect();
	}

	private byte[] getLightOffCommand(Device device)
	{
		RFXComLighting2Message msg = new RFXComLighting2Message();
		msg.subType = RFXComLighting2Message.SubType.AC;
		msg.sensorId = device.getSensorId();
		msg.unitcode = (byte) device.getUnitCode();
		msg.command = RFXComLighting2Message.Commands.OFF;
		msg.dimmingLevel = 0x0;

		return msg.decodeMessage();
	}

	private byte[] getLightOnCommand(Device device)
	{
		RFXComLighting2Message msg = new RFXComLighting2Message();
		msg.subType = RFXComLighting2Message.SubType.AC;
		msg.sensorId = device.getSensorId();
		msg.unitcode = (byte) device.getUnitCode();
		msg.command = RFXComLighting2Message.Commands.ON;
		msg.dimmingLevel = 0xF;

		return msg.decodeMessage();
	}

	private byte[] getLightDimCommand(Device device, int dimPercentage)
	{
		RFXComLighting2Message msg = new RFXComLighting2Message();
		msg.subType = RFXComLighting2Message.SubType.AC;
		msg.sensorId = device.getSensorId();
		msg.unitcode = (byte) device.getUnitCode();
		msg.command = RFXComLighting2Message.Commands.SET_LEVEL;
		msg.dimmingLevel = (byte) getDimLevelFromPercentType(dimPercentage);

		return msg.decodeMessage();
	}

	int getDimLevelFromPercentType(int percentage)
	{
		return new BigDecimal(percentage).multiply(BigDecimal.valueOf(15)).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_UP).intValue();
	}
}
