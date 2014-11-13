package no.home.automation.model;

import java.util.List;

import org.joda.time.LocalTime;

public class Rule
{
	public enum WHEN
	{
		ON, OFF, TIME;
		
		public static WHEN getEnum(String name)
		{
			for (WHEN re : WHEN.values())
			{
				if (re.toString().compareTo(name) == 0)
				{
					return re;
				}
			}
			throw new IllegalArgumentException("Invalid WHEN value: " + name);
		}
	};

	private int					id;
	private String				name;
	private boolean				active;
	private int					whenDeviceId;
	private WHEN				whenAction;
	private LocalTime			whenTime;
	private List<RuleThen>		thenList;
	private List<RuleCondition>	conditionList;

	public Rule(int id, String name, boolean active, int whenDeviceId, WHEN whenAction, LocalTime whenTime, List<RuleThen> thenList,
			List<RuleCondition> conditionList)
	{
		super();
		this.id = id;
		this.name = name;
		this.active = active;
		this.whenDeviceId = whenDeviceId;
		this.whenAction = whenAction;
		this.whenTime = whenTime;
		this.thenList = thenList;
		this.conditionList = conditionList;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public int getWhenDeviceId()
	{
		return whenDeviceId;
	}

	public void setWhenDeviceId(int whenDeviceId)
	{
		this.whenDeviceId = whenDeviceId;
	}

	public WHEN getWhenAction()
	{
		return whenAction;
	}

	public void setWhenAction(WHEN whenAction)
	{
		this.whenAction = whenAction;
	}

	public LocalTime getWhenTime()
	{
		return whenTime;
	}

	public void setWhenTime(LocalTime whenTime)
	{
		this.whenTime = whenTime;
	}

	public List<RuleThen> getThenList()
	{
		return thenList;
	}

	public void setThenList(List<RuleThen> thenList)
	{
		this.thenList = thenList;
	}

	public List<RuleCondition> getConditionList()
	{
		return conditionList;
	}

	public void setConditionList(List<RuleCondition> conditionList)
	{
		this.conditionList = conditionList;
	}
}
