package no.home.automation.rfxcom;

import java.util.EventListener;
import java.util.EventObject;

public interface RFXComEventListener extends EventListener
{

	/**
	 * Procedure for receive raw data from RFXCOM controller.
	 * 
	 * @param data
	 *            Received raw data.
	 */
	void packetReceived(EventObject event, byte[] data);

}
