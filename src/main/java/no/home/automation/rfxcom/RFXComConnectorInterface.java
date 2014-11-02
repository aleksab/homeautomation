package no.home.automation.rfxcom;

import java.io.IOException;

public interface RFXComConnectorInterface
{

	/**
	 * Procedure for connecting to RFXCOM controller.
	 * 
	 * @param device
	 *            Controller connection parameters (e.g. serial port name or IP address).
	 */
	public void connect(String device) throws Exception;

	/**
	 * Procedure for disconnecting to RFXCOM controller.
	 */
	public void disconnect();

	/**
	 * Procedure for send raw data to RFXCOM controller.
	 * 
	 * @param data
	 *            raw bytes.
	 */
	public void sendMessage(byte[] data) throws IOException;

	/**
	 * Procedure for register event listener.
	 * 
	 * @param listener
	 *            Event listener instance to handle events.
	 */
	public void addEventListener(RFXComEventListener listener);

	/**
	 * Procedure for remove event listener.
	 * 
	 * @param listener
	 *            Event listener instance to remove.
	 */
	public void removeEventListener(RFXComEventListener listener);

}