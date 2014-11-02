package no.home.automation.model;


public class UpdateDeviceResponse extends DefaultReponse
{
	private Device	device;

	public UpdateDeviceResponse(Device device, boolean result)
	{
		super(result);
		this.device = device;
	}

	public Device getDevice()
	{
		return device;
	}

	public void setDevice(Device device)
	{
		this.device = device;
	}
}
