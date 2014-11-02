package no.home.automation.rfxcom.messages;

import no.home.automation.rfxcom.messages.RFXComBaseMessage.PacketType;

public class RFXComMessageUtils {

	/**
	 * Command to reset RFXCOM controller.
	 * 
	 */
	public final static byte[] CMD_RESET = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to get RFXCOM controller status.
	 * 
	 */
	public final static byte[] CMD_STATUS = new byte[] { 0x0D, 0x00, 0x00,
			0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to save RFXCOM controller configuration.
	 * 
	 */
	public final static byte[] CMD_SAVE = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	public static RFXComBaseMessage decodePacket(byte[] data) throws IllegalArgumentException {

		RFXComBaseMessage obj = null;

		byte packetType = data[1];

		switch (packetType) {
		case (byte) 0x00:
			obj = new RFXComControlMessage(data);
			break;
		case (byte) 0x01:
			obj = new RFXComInterfaceMessage(data);
			break;
		case (byte) 0x02:
			obj = new RFXComTransmitterMessage(data);
			break;
		case (byte) 0x10:
			obj = new RFXComLighting1Message(data);
			break;
		case (byte) 0x11:
			obj = new RFXComLighting2Message(data);
			break;
//		case (byte) 0x18:
//			obj = new RFXComCurtain1Message(data);
//			break;
		case (byte) 0x52:
			obj = new RFXComTemperatureHumidityMessage(data);
			break;

		default:
			throw new IllegalArgumentException("Packet type " + (int) packetType
					+ " not implemented!");
		}

		return obj;
	}

	public static byte[] encodePacket(Object obj)  throws IllegalArgumentException {

		byte[] data = null;

		if (obj instanceof RFXComBaseMessage)
			data = ((RFXComBaseMessage) obj).decodeMessage();

		if( data == null ) {
			throw new IllegalArgumentException("No valid encoder implemented!");
		}
			
		return data;
	}

	public static PacketType convertPacketType(String packetType)
			throws IllegalArgumentException {

		for (PacketType p : PacketType.values()) {
			if (p.toString().equals(packetType)) {
				return p;
			}
		}

		throw new IllegalArgumentException("Unknown packet type " + packetType);
	}

	public static Object convertSubType(PacketType packetType, String subType) {

		switch (packetType) {

		case LIGHTING1:
			for (RFXComLighting1Message.SubType s : RFXComLighting1Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

		case LIGHTING2:
			for (RFXComLighting2Message.SubType s : RFXComLighting2Message.SubType.values()) {
				if (s.toString().equals(subType)) {
					return s;
				}
			}
			break;

//		case CURTAIN1:
//			for (RFXComCurtain1Message.SubType s : RFXComCurtain1Message.SubType.values()) {
//				if (s.toString().equals(subType)) {
//					return s;
//				}
//			}
//			break;

		case TEMPERATURE_HUMIDITY:
			break;

		case INTERFACE_CONTROL:
		case INTERFACE_MESSAGE:
		case UNKNOWN:
		default:
			break;

		}

		throw new IllegalArgumentException("Unknown sub type " + subType);
	}
	
	
}