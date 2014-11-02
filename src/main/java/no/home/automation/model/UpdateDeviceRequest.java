package no.home.automation.model;

import org.springframework.util.StringUtils;

public class UpdateDeviceRequest extends RequestValidator
{
	public enum TYPE
	{
		ADD, DELETE, RENAME
	};

	private TYPE	type;
	private int		sensorId;
	private int		unitCode;
	private String	name;

	public UpdateDeviceRequest(TYPE type, int sensorId, int unitCode, String name)
	{
		super();
		this.type = type;
		this.sensorId = sensorId;
		this.unitCode = unitCode;
		this.name = name;
	}

	public TYPE getType()
	{
		return type;
	}

	public void setType(TYPE type)
	{
		this.type = type;
	}

	public int getSensorId()
	{
		return sensorId;
	}

	public void setSensorId(int sensorId)
	{
		this.sensorId = sensorId;
	}

	public int getUnitCode()
	{
		return unitCode;
	}

	public void setUnitCode(int unitCode)
	{
		this.unitCode = unitCode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{
		if (sensorId == 0)
			throw new IllegalArgumentException("unitCode is missing");
		if (sensorId == 0)
			throw new IllegalArgumentException("unitId is missing");
		if (StringUtils.isEmpty(name))
			throw new IllegalArgumentException("NAme is missing");
	}
}
