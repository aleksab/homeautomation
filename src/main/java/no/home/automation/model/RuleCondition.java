package no.home.automation.model;

import org.joda.time.LocalTime;

public class RuleCondition
{
	public enum CONDITION
	{
		TIME_OF_DAY, DAY_OF_WEEK, DELAY_MINUTES, FROM_TO_TIME, FROM_TO_DAY_OF_WEEK;

		public static CONDITION getEnum(String name)
		{
			for (CONDITION re : CONDITION.values())
			{
				if (re.toString().compareTo(name) == 0)
				{
					return re;
				}
			}
			throw new IllegalArgumentException("Invalid CONDITION value: " + name);
		}
	};

	private int			id;
	private CONDITION	condition;
	private LocalTime	timeOfDay;
	private int			dayOfWeek;
	private LocalTime	fromTime;
	private LocalTime	toTime;
	private int			fromDayOfWeek;
	private int			toDayOfWeek;

	public RuleCondition(int id, CONDITION condition, LocalTime timeOfDay, int dayOfWeek, LocalTime fromTime, LocalTime toTime, int fromDayOfWeek,
			int toDayOfWeek)
	{
		super();
		this.id = id;
		this.condition = condition;
		this.timeOfDay = timeOfDay;
		this.dayOfWeek = dayOfWeek;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.fromDayOfWeek = fromDayOfWeek;
		this.toDayOfWeek = toDayOfWeek;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public CONDITION getCondition()
	{
		return condition;
	}

	public void setCondition(CONDITION condition)
	{
		this.condition = condition;
	}

	public LocalTime getTimeOfDay()
	{
		return timeOfDay;
	}

	public void setTimeOfDay(LocalTime timeOfDay)
	{
		this.timeOfDay = timeOfDay;
	}

	public int getDayOfWeek()
	{
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getFromTime()
	{
		return fromTime;
	}

	public void setFromTime(LocalTime fromTime)
	{
		this.fromTime = fromTime;
	}

	public LocalTime getToTime()
	{
		return toTime;
	}

	public void setToTime(LocalTime toTime)
	{
		this.toTime = toTime;
	}

	public int getFromDayOfWeek()
	{
		return fromDayOfWeek;
	}

	public void setFromDayOfWeek(int fromDayOfWeek)
	{
		this.fromDayOfWeek = fromDayOfWeek;
	}

	public int getToDayOfWeek()
	{
		return toDayOfWeek;
	}

	public void setToDayOfWeek(int toDayOfWeek)
	{
		this.toDayOfWeek = toDayOfWeek;
	}

	public void validate()
	{
		if (condition == CONDITION.TIME_OF_DAY && timeOfDay == null)
			throw new IllegalArgumentException("timeOfDay is missing");
		if (condition == CONDITION.DAY_OF_WEEK && (dayOfWeek < 0 || dayOfWeek > 6))
			throw new IllegalArgumentException("dayOfWeek is incorrect");
		if (condition == CONDITION.FROM_TO_TIME && (fromTime == null || toTime == null))
			throw new IllegalArgumentException("from/to is incorrect");
		if (condition == CONDITION.FROM_TO_DAY_OF_WEEK && ((fromDayOfWeek < 0 || fromDayOfWeek > 6) || (toDayOfWeek < 0 || toDayOfWeek > 6)))
			throw new IllegalArgumentException("from/to is incorrect");
	}
}
