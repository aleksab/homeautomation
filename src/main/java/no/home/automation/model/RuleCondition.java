package no.home.automation.model;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class RuleCondition
{
	public enum CONDITION
	{
		TIME_OF_DAY, DAY_OF_WEEK, FROM_TO_TIME, FROM_TO_DAY_OF_WEEK, DELAY_MINUTES;

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
	private int			delayInMinutes;
	private DateTime	from;
	private DateTime	to;

	public RuleCondition(int id, CONDITION condition, LocalTime timeOfDay, int dayOfWeek, int delayInMinutes, DateTime from, DateTime to)
	{
		super();
		this.id = id;
		this.condition = condition;
		this.timeOfDay = timeOfDay;
		this.dayOfWeek = dayOfWeek;
		this.delayInMinutes = delayInMinutes;
		this.from = from;
		this.to = to;
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

	public int getDelayInMinutes()
	{
		return delayInMinutes;
	}

	public void setDelayInMinutes(int delayInMinutes)
	{
		this.delayInMinutes = delayInMinutes;
	}

	public DateTime getFrom()
	{
		return from;
	}

	public void setFrom(DateTime from)
	{
		this.from = from;
	}

	public DateTime getTo()
	{
		return to;
	}

	public void setTo(DateTime to)
	{
		this.to = to;
	}

	public void validate()
	{
		if (condition == CONDITION.TIME_OF_DAY && timeOfDay == null)
			throw new IllegalArgumentException("timeOfDay is missing");
		if (condition == CONDITION.DAY_OF_WEEK && (dayOfWeek < 0 || dayOfWeek > 6))
			throw new IllegalArgumentException("dayOfWeek is incorrect");
		if (condition == CONDITION.DELAY_MINUTES && (delayInMinutes <= 0 || delayInMinutes > 360))
			throw new IllegalArgumentException("delayInMinutes is incorrect");
		if ((condition == CONDITION.FROM_TO_TIME || condition == CONDITION.FROM_TO_DAY_OF_WEEK) && (from == null || to == null))
			throw new IllegalArgumentException("from/to is incorrect");
	}
}
