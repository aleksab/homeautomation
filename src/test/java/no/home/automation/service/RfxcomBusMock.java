package no.home.automation.service;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.home.automation.model.Device;
import no.home.automation.rfxcom.messages.RFXComBaseMessage.PacketType;
import no.home.automation.rfxcom.messages.RFXComLighting2Message;

public class RfxcomBusMock implements RfxcomBus
{
	protected static final Logger		logger			= LoggerFactory.getLogger(RfxcomBusMock.class);

	private List<RfxcomEventListener>	listenerList	= null;
	private boolean						sendLightEvents	= false;

	public RfxcomBusMock()
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
	public boolean sendLightOnCommand(Device device)
	{
		return true;
	}

	@Override
	public boolean sendLightOffCommand(Device device)
	{
		return true;
	}

	@Override
	public boolean sendLightDimCommand(Device device, int dimPercentage)
	{
		return true;
	}

	public void sendLightEvents(int startSeconds, int secondsIntveral)
	{
		if (sendLightEvents)
			return;

		sendLightEvents = true;

		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(startSeconds * 1000);

					while (sendLightEvents)
					{
						RFXComLighting2Message message = new RFXComLighting2Message();
						message.unitcode = 12;
						message.sensorId = 1234;
						message.packetType = PacketType.LIGHTING2;
						message.rawMessage = "TEST".getBytes();

						logger.info("Sending light event");
						for (RfxcomEventListener listener : listenerList)
						{
							listener.packetReceived(message);
						}

						Thread.sleep(secondsIntveral * 1000);
					}
				}
				catch (InterruptedException e)
				{
				}
			}
		}).start();
	}

	public void stopLightEvents()
	{
		sendLightEvents = false;

		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
		}
	}
}
