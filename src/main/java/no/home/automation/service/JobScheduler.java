package no.home.automation.service;

import no.home.automation.model.Device;

public interface JobScheduler
{
	public void scheduleLightOff(Device device);
}
