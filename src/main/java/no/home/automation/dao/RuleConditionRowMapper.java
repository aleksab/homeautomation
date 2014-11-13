package no.home.automation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import no.home.automation.model.RuleCondition;
import no.home.automation.model.RuleCondition.CONDITION;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class RuleConditionRowMapper implements ParameterizedRowMapper<RuleCondition>
{
	@Override
	public RuleCondition mapRow(ResultSet resultSet, int i) throws SQLException
	{
		return new RuleCondition(resultSet.getInt("Id"), CONDITION.getEnum(resultSet.getString("Condition")), new LocalTime(
				resultSet.getBoolean("TimeOfDay")), resultSet.getInt("DayOfWeek"), resultSet.getInt("DelayInMinutes"), new DateTime(
				resultSet.getString("From")), new DateTime(resultSet.getString("To")));
	}
}
