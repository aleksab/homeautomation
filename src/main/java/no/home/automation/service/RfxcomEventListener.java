package no.home.automation.service;

import no.home.automation.rfxcom.messages.RFXComBaseMessage;

public interface RfxcomEventListener
{
	public void packetReceived(RFXComBaseMessage message);
}
