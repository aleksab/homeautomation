package no.home.automation.service;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.LinkedList;
import java.util.List;

import no.home.automation.model.Device;

public class JobSchedulerImpl implements JobScheduler, Runnable
{
	private RfxcomBus		bus;
	private List<OffTime>	devices;

	public JobSchedulerImpl(RfxcomBus bus)
	{
		this.bus = bus;
		devices = new LinkedList<>();
	}

	public void startScheduler()
	{
		Scheduler scheduler = new Scheduler();
		scheduler.schedule("0/5 * * * *", this);
		scheduler.start();
	}

	@Override
	public void scheduleLightOff(Device device)
	{
		if (device.getTurnOffAfter() != 0)
		{
			long expiredTime = System.currentTimeMillis() + (device.getTurnOffAfter() * 1000);
			boolean found = false;

			for (OffTime offTime : devices)
			{
				if (offTime.device.equals(device))
				{
					// update time
					offTime.expireTime = expiredTime;
					found = true;
					break;
				}
			}

			if (!found)
			{
				devices.add(new OffTime(device, expiredTime));
			}
		}
	}

	@Override
	public void run()
	{
		List<OffTime> completed = new LinkedList<>();
		for (OffTime offTime : devices)
		{
			if (offTime.expireTime >= System.currentTimeMillis())
			{
				completed.add(offTime);
				bus.sendLightOffCommand(offTime.device);
			}
		}

		devices.removeAll(completed);
	}

	class OffTime
	{
		Device	device;
		long	expireTime;

		public OffTime(Device device, long expireTime)
		{
			super();
			this.device = device;
			this.expireTime = expireTime;
		}
	}
}
