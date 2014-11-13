package no.home.automation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import no.home.automation.model.RuleCondition;
import no.home.automation.model.RuleCondition.CONDITION;

import org.joda.time.LocalTime;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class RuleConditionRowMapper implements ParameterizedRowMapper<RuleCondition>
{
	@Override
	public RuleCondition mapRow(ResultSet resultSet, int i) throws SQLException
	{		
		return new RuleCondition(resultSet.getInt("Id"), CONDITION.getEnum(resultSet.getString("Condition")),
				getTime(resultSet.getString("TimeOfDay")), resultSet.getInt("DayOfWeek"), resultSet.getInt("DelayInMinutes"),
				getTime(resultSet.getString("FromTime")), getTime(resultSet.getString("ToTime")), resultSet.getInt("FromDayOfWeek"),
				resultSet.getInt("ToDayOfWeek"));
	}

	private LocalTime getTime(String input)
	{
		try
		{
			return LocalTime.parse(input);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
