package no.home.automation.model;


public class RuleThen
{
	public enum THEN
	{
		ON, OFF, DIM;

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
	private int		dimLevel;

	public RuleThen(int id, THEN action, int deviceId, int dimLevel)
	{
		super();
		this.id = id;
		this.action = action;
		this.deviceId = deviceId;
		this.dimLevel = dimLevel;
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

	public int getDimLevel()
	{
		return dimLevel;
	}

	public void setDimLevel(int dimLevel)
	{
		this.dimLevel = dimLevel;
	}

	public void validate() throws IllegalArgumentException
	{
		if (deviceId == 0)
			throw new IllegalArgumentException("deviceId is missing");
	}
}
