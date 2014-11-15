package no.home.automation.model;

import java.util.List;

public class SearchDeviceResponse extends DefaultReponse
{
	private List<Device>	devices;

	public SearchDeviceResponse(List<Device> devices)
	{
		this.devices = devices;
	}

	public List<Device> getDevices()
	{
		return devices;
	}

	public void setDevices(List<Device> devices)
	{
		this.devices = devices;
	}
}
