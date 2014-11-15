package no.home.automation.model;

import java.util.List;

public class ListDeviceResponse extends DefaultReponse
{
	private List<Device>	devices;

	public ListDeviceResponse(List<Device> devices)
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
