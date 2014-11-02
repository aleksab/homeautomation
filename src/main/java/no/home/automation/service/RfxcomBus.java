package no.home.automation.service;

import no.home.automation.model.Device;

public interface RfxcomBus
{
	public void addEventListener(RfxcomEventListener listener);

	public void removeEventListener(RfxcomEventListener listener);

	public boolean sendLightOnCommand(Device device);

	public boolean sendLightOffCommand(Device device);

	public boolean sendLightDimCommand(Device device, int dimPercentage);
}
