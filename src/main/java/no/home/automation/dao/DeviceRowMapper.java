package no.home.automation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import no.home.automation.model.Device;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class DeviceRowMapper implements ParameterizedRowMapper<Device>
{
	@Override
	public Device mapRow(ResultSet resultSet, int i) throws SQLException
	{
		return new Device(resultSet.getInt("Id"), resultSet.getInt("SensorId"), resultSet.getInt("UnitCode"), resultSet.getString("Name"));
	}
}
