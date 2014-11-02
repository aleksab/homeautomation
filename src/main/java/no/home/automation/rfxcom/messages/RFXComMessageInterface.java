package no.home.automation.rfxcom.messages;

public interface RFXComMessageInterface {

	/**
	 * Procedure for present class information in string format. Used for
	 * logging purposes.
	 * 
	 */
	String toString();

	/**
	 * Procedure for encode raw data.
	 * 
	 * @param data
	 *            Raw data.
	 */
	void encodeMessage(byte[] data);

	/**
	 * Procedure for decode object to raw data.
	 * 
	 * @return raw data.
	 */
	byte[] decodeMessage();
}
