package no.home.automation.ws.action;

import java.util.List;

import no.home.automation.model.Device;
import no.home.automation.service.RfxcomBusMock;

import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SearchDevicesActionTest
{
	private static SearchDevicesAction	action	= null;
	private static RfxcomBusMock		bus;

	@BeforeClass
	public static void setup() throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");

		bus = new RfxcomBusMock();		

		action = new SearchDevicesAction(false, bus);
	}

	@AfterClass
	public static void teardown() throws Exception
	{
		bus.stopLightEvents();
	}

	@Test
	public void testGetActiveDevices()
	{
		bus.sendLightEvents(2, 1, true);
		List<Device> devices = action.getActiveDevices();
		Assert.assertEquals(3, devices.size());
		bus.stopLightEvents();
	}
	
	@Test
	public void testGetActiveDevices2()
	{
		bus.sendLightEvents(1, 1, true);
		List<Device> devices = action.getActiveDevices();
		Assert.assertEquals(4, devices.size());
		bus.stopLightEvents();
	}
	
	@Test
	public void testGetActiveDevices3()
	{
		bus.sendLightEvents(2, 1, false);
		List<Device> devices = action.getActiveDevices();
		Assert.assertEquals(1, devices.size());
		bus.stopLightEvents();
	}
}
