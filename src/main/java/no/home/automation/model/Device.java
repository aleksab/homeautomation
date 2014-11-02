package no.home.automation.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Device
{
	private int		sensorId;
	private int		unitCode;
	private String	name;
	private int		turnOffAfter;

	public Device(int sensorId, int unitCode, String name, int turnOffAfter)
	{
		super();
		this.sensorId = sensorId;
		this.unitCode = unitCode;
		this.name = name;
		this.turnOffAfter = turnOffAfter;
	}

	public int getTurnOffAfter()
	{
		return turnOffAfter;
	}

	public void setTurnOffAfter(int turnOffAfter)
	{
		this.turnOffAfter = turnOffAfter;
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
	public boolean equals(Object obj)
	{
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
