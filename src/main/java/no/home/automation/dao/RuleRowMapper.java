package no.home.automation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import no.home.automation.model.Rule;
import no.home.automation.model.Rule.WHEN;

import org.joda.time.LocalTime;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class RuleRowMapper implements ParameterizedRowMapper<Rule>
{
	@Override
	public Rule mapRow(ResultSet resultSet, int i) throws SQLException
	{
		return new Rule(resultSet.getInt("RuleId"), resultSet.getString("Name"), resultSet.getBoolean("Active"), resultSet.getInt("WhenDeviceId"),
				WHEN.getEnum(resultSet.getString("WhenAction")), new LocalTime(resultSet.getString("WhenTime")), null, null);
	}
}
