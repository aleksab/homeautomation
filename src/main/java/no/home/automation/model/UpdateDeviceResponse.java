package no.home.automation.model;

public class UpdateDeviceResponse extends DefaultReponse
{
	private Device	device;
	private String	message;

	public UpdateDeviceResponse(Device device, String message, boolean result)
	{
		super(result);
		this.device = device;
		this.message = message;
	}

	public Device getDevice()
	{
		return device;
	}

	public void setDevice(Device device)
	{
		this.device = device;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
