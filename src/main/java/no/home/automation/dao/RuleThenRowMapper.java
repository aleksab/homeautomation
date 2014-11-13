package no.home.automation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import no.home.automation.model.RuleThen;
import no.home.automation.model.RuleThen.THEN;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class RuleThenRowMapper implements ParameterizedRowMapper<RuleThen>
{
	@Override
	public RuleThen mapRow(ResultSet resultSet, int i) throws SQLException
	{
		return new RuleThen(resultSet.getInt("Id"), THEN.getEnum(resultSet.getString("Action")), resultSet.getInt("DeviceId"),
				resultSet.getInt("DimLevel"));
	}
}
