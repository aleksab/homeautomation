package no.home.automation.model;

public class RuleThen
{
	public enum THEN
	{
		ON, OFF, DIM, WAIT_OFF;

		public static THEN getEnum(String name)
		{
			for (THEN re : THEN.values())
			{
				if (re.toString().compareTo(name) == 0)
				{
					return re;
				}
			}
			throw new IllegalArgumentException("Invalid THEN value: " + name);
		}
	};

	private int		id;
	private THEN	action;
	private int		deviceId;
	private int		value;

	public RuleThen(int id, THEN action, int deviceId, int value)
	{
		super();
		this.id = id;
		this.action = action;
		this.deviceId = deviceId;
		this.value = value;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public THEN getAction()
	{
		return action;
	}

	public void setAction(THEN action)
	{
		this.action = action;
	}

	public int getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(int deviceId)
	{
		this.deviceId = deviceId;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public void validate() throws IllegalArgumentException
	{
		if (deviceId == 0)
			throw new IllegalArgumentException("deviceId is missing");
	}
}
