package no.home.automation.rfxcom.messages;

public class RFXComControlMessage extends RFXComBaseMessage {

	public RFXComControlMessage() {

	}

	public RFXComControlMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public byte[] decodeMessage() {
		return null;
	}

	@Override
	public void encodeMessage(byte[] data) {
		super.encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();

		return str;
	}
}
