package no.home.automation.rfxcom;

import java.util.EventObject;

public class RFXComMessageReceivedEvent extends EventObject
{

	private static final long	serialVersionUID	= 3821740012020068392L;

	public RFXComMessageReceivedEvent(Object source)
	{
		super(source);
	}

	/**
	 * Invoked when data message is received from RFXCOM controller.
	 * 
	 * @param packet
	 *            Data from controller.
	 */
	public void MessageReceivedEvent(byte[] packet)
	{
	}

}