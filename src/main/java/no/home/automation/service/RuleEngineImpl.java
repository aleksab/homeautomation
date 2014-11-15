package no.home.automation.service;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.LinkedList;
import java.util.List;

import no.home.automation.dao.DeviceRowMapper;
import no.home.automation.dao.RuleConditionRowMapper;
import no.home.automation.dao.RuleRowMapper;
import no.home.automation.dao.RuleThenRowMapper;
import no.home.automation.model.Device;
import no.home.automation.model.Rule;
import no.home.automation.model.Rule.WHEN;
import no.home.automation.model.RuleCondition;
import no.home.automation.model.RuleCondition.CONDITION;
import no.home.automation.model.RuleThen;
import no.home.automation.model.RuleThen.THEN;
import no.home.automation.rfxcom.messages.RFXComBaseMessage;
import no.home.automation.rfxcom.messages.RFXComBaseMessage.PacketType;
import no.home.automation.rfxcom.messages.RFXComLighting2Message;
import no.home.automation.rfxcom.messages.RFXComLighting2Message.Commands;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class RuleEngineImpl implements Runnable, RuleEngine, RfxcomEventListener
{
	private static final Logger	logger		= LoggerFactory.getLogger("fileLogger");

	private JdbcTemplate		jdbcTemplate;
	private Scheduler			scheduler;
	private RfxcomBus			bus;

	private List<Rule>			timeRules;
	private List<Rule>			onRules;
	private List<Rule>			offRules;

	private boolean				keepRunning	= false;

	public RuleEngineImpl(JdbcTemplate jdbcTemplate, RfxcomBus bus)
	{
		this.jdbcTemplate = jdbcTemplate;
		this.bus = bus;

		timeRules = new LinkedList<>();
		onRules = new LinkedList<>();
		offRules = new LinkedList<>();

		scheduler = new Scheduler();
		scheduler.schedule("/1 * * * *", this);
	}

	public void startEngine()
	{
		reloadEngine();

		// listen to packets
		bus.addEventListener(this);

		// listen to timer
		scheduler.start();

		keepRunning = true;
		new Thread(this).start();
	}

	public void reloadEngine()
	{
		timeRules.clear();
		onRules.clear();
		offRules.clear();
		List<Rule> rules = getAllRules();

		for (Rule rule : rules)
		{
			if (rule.getWhenAction() == WHEN.ON)
				onRules.add(rule);
			else if (rule.getWhenAction() == WHEN.OFF)
				offRules.add(rule);
			else if (rule.getWhenAction() == WHEN.TIME)
				timeRules.add(rule);
		}
	}

	public void stopEngine()
	{
		bus.removeEventListener(this);

		scheduler.stop();

		keepRunning = false;
	}

	@Override
	public void run()
	{
		while (keepRunning)
		{
			logger.info("Checking time rules {}", timeRules.size());
			for (Rule rule : timeRules)
			{
				LocalTime now = LocalTime.now();
				LocalTime time = rule.getWhenTime();

				if (now.getHourOfDay() == time.getHourOfDay() && now.getMinuteOfHour() == time.getMinuteOfHour())
				{
					Device device = findDevice(rule.getWhenDeviceId());
					triggerRule(device, rule);
				}
			}

			try
			{
				Thread.sleep(1000 * 60);
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	@Override
	public void packetReceived(RFXComBaseMessage message)
	{
		if (message.packetType == PacketType.LIGHTING2)
		{
			RFXComLighting2Message lightMessage = (RFXComLighting2Message) message;

			logger.info("sensor: " + lightMessage.sensorId);
			logger.info("unitcode: " + lightMessage.unitcode);

			Device device = findDevice(lightMessage.sensorId, lightMessage.unitcode);

			if (lightMessage.command == Commands.ON || lightMessage.command == Commands.SET_LEVEL)
			{
				for (Rule rule : onRules)
				{
					if (rule.getWhenDeviceId() == device.getId())
						triggerRule(device, rule);
				}
			}
			else if (lightMessage.command == Commands.OFF)
			{
				for (Rule rule : offRules)
				{
					if (rule.getWhenDeviceId() == device.getId())
						triggerRule(device, rule);
				}
			}
		}
	}

	void triggerRule(final Device device, final Rule rule)
	{
		logger.info("Rule {} triggered for device {}", rule.getId(), device);

		new Thread(new Runnable()
		{
			public void run()
			{
				if (shouldTriggerActions(rule.getConditionList()))
				{
					logger.info("All conditions for rule {} are ok, so performing actions", rule.getId());
					triggerAction(rule.getThenList());
				}
				else
					logger.info("Not all conditions are ok, so not doing action");
			}
		}).start();
	}

	/**
	 * Should we trigger all actions? All conditions must be fulfilled
	 * 
	 * @param conditionList
	 * @return
	 */
	boolean shouldTriggerActions(List<RuleCondition> conditionList)
	{
		for (RuleCondition rule : conditionList)
		{
			if (!isConditionValid(rule))
				return false;
		}

		return true;
	}

	/**
	 * Check if a condition is valid or not.
	 * 
	 * @param condition
	 * @return
	 */
	boolean isConditionValid(RuleCondition condition)
	{
		if (condition.getCondition() == CONDITION.DAY_OF_WEEK)
		{
			int now = DateTime.now().getDayOfWeek();
			return (now == condition.getDayOfWeek());
		}
		else if (condition.getCondition() == CONDITION.TIME_OF_DAY)
		{
			LocalTime now = LocalTime.now();
			LocalTime time = condition.getTimeOfDay();
			return (now.getHourOfDay() == time.getHourOfDay() && now.getMinuteOfHour() == time.getMinuteOfHour());
		}
		else if (condition.getCondition() == CONDITION.FROM_TO_DAY_OF_WEEK)
		{
			int now = DateTime.now().getDayOfWeek();
			return (condition.getFromDayOfWeek() <= now && now <= condition.getToDayOfWeek());
		}
		else if (condition.getCondition() == CONDITION.FROM_TO_TIME)
		{
			LocalTime from = condition.getFromTime();
			LocalTime to = condition.getToTime();
			LocalTime now = LocalTime.now();

			return (now.isAfter(from) && now.isBefore(to));
		}

		return false;
	}

	/**
	 * Trigger all actions
	 * 
	 * @param thenList
	 */
	void triggerAction(List<RuleThen> thenList)
	{
		for (RuleThen rule : thenList)
		{
			Device device = findDevice(rule.getDeviceId());
			if (device == null)
			{
				logger.error("Could not find device {}", rule.getDeviceId());
				continue;
			}

			if (rule.getAction() == THEN.ON)
				bus.sendLightOnCommand(device);
			else if (rule.getAction() == THEN.OFF)
				bus.sendLightOffCommand(device);
			else if (rule.getAction() == THEN.DIM)
				bus.sendLightDimCommand(device, rule.getValue());
			else if (rule.getAction() == THEN.WAIT_OFF)
			{
				try
				{
					Thread.sleep(1000 * 60 * rule.getValue());
					bus.sendLightOffCommand(device);
				}
				catch (InterruptedException ex)
				{
					logger.error("Could not sleep", ex);
				}
			}
		}
	}

	Device findDevice(int deviceId)
	{
		try
		{
			return jdbcTemplate.queryForObject("SELECT * FROM device WHERE Id=?", new DeviceRowMapper(), deviceId);
		}
		catch (Exception ex)
		{
			logger.error("Could not find device", ex);
			return null;
		}
	}

	Device findDevice(int sensorId, int unitCode)
	{
		try
		{
			return jdbcTemplate.queryForObject("SELECT * FROM device WHERE SensorId=? and UnitCode=?", new DeviceRowMapper(), sensorId, unitCode);
		}
		catch (Exception ex)
		{
			logger.error("Could not find device", ex);
			return null;
		}
	}

	private List<Rule> getAllRules()
	{
		List<Rule> rules = jdbcTemplate.query("SELECT * FROM rule", new RuleRowMapper());

		for (Rule rule : rules)
		{
			List<RuleThen> thenList = jdbcTemplate.query("SELECT * FROM rule_then WHERE ruleId=?", new RuleThenRowMapper(), rule.getId());
			List<RuleCondition> conditionList = jdbcTemplate.query("SELECT * FROM rule_condition WHERE ruleId=?", new RuleConditionRowMapper(),
					rule.getId());

			rule.setThenList(thenList);
			rule.setConditionList(conditionList);
		}

		return rules;
	}
}
