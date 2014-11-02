package no.home.automation.model;

public class SendCommandRequest extends RequestValidator
{
	public enum TYPE
	{
		ON, OFF, DIM
	};

	private TYPE	type;
	private int		dimLevel;
	private int		sensorId;
	private int		unitCode;

	public SendCommandRequest(TYPE type, int dimLevel, int sensorId, int unitCode)
	{
		super();
		this.type = type;
		this.dimLevel = dimLevel;
		this.sensorId = sensorId;
		this.unitCode = unitCode;
	}

	public TYPE getType()
	{
		return type;
	}

	public void setType(TYPE type)
	{
		this.type = type;
	}

	public int getDimLevel()
	{
		return dimLevel;
	}

	public void setDimLevel(int dimLevel)
	{
		this.dimLevel = dimLevel;
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

	@Override
	public void validateRequest() throws IllegalArgumentException
	{
		if (type == TYPE.DIM)
		{
			if (dimLevel < 0 || dimLevel > 100)
				throw new IllegalArgumentException("Dim level must be between 0 and 100");
		}
	}
}
