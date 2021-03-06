package no.home.automation.rfxcom.messages;

public class RFXComTransmitterMessage extends RFXComBaseMessage {

    public enum SubType {
            TRANSMITTER_MESSAGE(1),

            UNKNOWN(255);

            private final int subType;

            SubType(int subType) {
                    this.subType = subType;
            }

            SubType(byte subType) {
                    this.subType = subType;
            }

            public byte toByte() {
                    return (byte) subType;
            }
    }

    public enum Response {
            ACK(0), // ACK, transmit OK
            ACK_DELAYED(1), // ACK, but transmit started after 3 seconds delay
                                            // anyway with RF receive data
            NAK(2), // NAK, transmitter did not lock on the requested transmit
                            // frequency
            NAK_INVALID_AC_ADDRESS(3), // NAK, AC address zero in id1-id4 not
                                                                    // allowed

            UNKNOWN(255);

            private final int response;

            Response(int response) {
                    this.response = response;
            }

            Response(byte response) {
                    this.response = response;
            }

            public byte toByte() {
                    return (byte) response;
            }
    }

    public SubType subType = SubType.TRANSMITTER_MESSAGE;
    public Response response = Response.UNKNOWN;

    public RFXComTransmitterMessage() {
            packetType = PacketType.TRANSMITTER_MESSAGE;

    }

    public RFXComTransmitterMessage(byte[] data) {

            encodeMessage(data);
    }

    @Override
    public String toString() {
            String str = "";

            str += super.toString();
            str += "\n - Sub type = " + subType;
            str += "\n - Response = " + response;

            return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

            super.encodeMessage(data);

            subType = SubType.values()[super.subType];
            response = Response.values()[data[4]];

    }

    @Override
    public byte[] decodeMessage() {

            byte[] data = new byte[5];

            data[0] = 0x04;
            data[1] = RFXComBaseMessage.PacketType.TRANSMITTER_MESSAGE.toByte();
            data[2] = subType.toByte();
            data[3] = seqNbr;
            data[4] = response.toByte();

            return data;
    }

}

