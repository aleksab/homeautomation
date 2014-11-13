package no.home.automation.ws.action;

import no.home.automation.dao.DeviceRowMapper;
import no.home.automation.model.DefaultReponse;
import no.home.automation.model.Device;
import no.home.automation.model.SendCommandRequest;
import no.home.automation.model.SendCommandRequest.TYPE;
import no.home.automation.service.RfxcomBusMock;

import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

public class SendCommandActionTest
{
	private static SendCommandAction	action	= null;
	private static RfxcomBusMock		bus;
	private static JdbcTemplate			jdbcTemplate;

	@BeforeClass
	public static void setup() throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");

		bus = new RfxcomBusMock();
		jdbcTemplate = Mockito.mock(JdbcTemplate.class);

		Device device = Mockito.mock(Device.class);
		Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(DeviceRowMapper.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(
				device);

		action = new SendCommandAction(false, bus, jdbcTemplate);
	}

	@AfterClass
	public static void teardown() throws Exception
	{
		bus.stopLightEvents();
	}

	@Test
	public void testSendOnCommand()
	{
		SendCommandRequest request = new SendCommandRequest(TYPE.ON, 0, 1, 1);

		DefaultReponse response = action.doHandle(request);

		Assert.assertTrue(response.isSuccess());
	}

}
