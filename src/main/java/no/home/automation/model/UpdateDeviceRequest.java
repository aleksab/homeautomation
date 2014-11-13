package no.home.automation.model;

import org.springframework.util.StringUtils;

public class UpdateDeviceRequest extends RequestValidator
{
	public enum TYPE
	{
		ADD, DELETE, RENAME
	};

	private TYPE	type;
	private Device	device;

	public UpdateDeviceRequest(TYPE type, Device device)
	{
		super();
		this.type = type;
		this.device = device;
	}

	public TYPE getType()
	{
		return type;
	}

	public void setType(TYPE type)
	{
		this.type = type;
	}

	public Device getDevice()
	{
		return device;
	}

	public void setDevice(Device device)
	{
		this.device = device;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{
		if (device.getId() == 0 && type != TYPE.ADD)
			throw new IllegalArgumentException("deviceId is missing");
		if (device.getUnitCode() == 0 && type == TYPE.ADD)
			throw new IllegalArgumentException("unitCode is missing");
		if (device.getSensorId() == 0 && type == TYPE.ADD)
			throw new IllegalArgumentException("unitId is missing");
		if (StringUtils.isEmpty(device.getName()) && type != TYPE.DELETE)
			throw new IllegalArgumentException("Name is missing");
	}
}
