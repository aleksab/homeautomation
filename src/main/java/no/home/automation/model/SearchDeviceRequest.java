package no.home.automation.model;

public class SearchDeviceRequest extends RequestValidator
{
	private boolean	shouldShowKnownDevices	= false;

	public boolean isShouldShowKnownDevices()
	{
		return shouldShowKnownDevices;
	}

	public void setShouldShowKnownDevices(boolean shouldShowKnownDevices)
	{
		this.shouldShowKnownDevices = shouldShowKnownDevices;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{

	}
}
