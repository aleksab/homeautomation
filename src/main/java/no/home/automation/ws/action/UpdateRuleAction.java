package no.home.automation.ws.action;

import java.util.HashMap;
import java.util.Map;

import no.home.automation.model.DefaultHandler;
import no.home.automation.model.Rule;
import no.home.automation.model.RuleCondition;
import no.home.automation.model.RuleThen;
import no.home.automation.model.UpdateRuleRequest;
import no.home.automation.model.UpdateRuleRequest.TYPE;
import no.home.automation.model.UpdateRuleResponse;
import no.home.automation.ws.LocalTimeTypeConverter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UpdateRuleAction extends DefaultHandler<UpdateRuleRequest, UpdateRuleResponse>
{
	private JdbcTemplate	jdbcTemplate	= null;

	public UpdateRuleAction(boolean mustBeAuthenticated, JdbcTemplate jdbcTemplate)
	{
		super(mustBeAuthenticated);
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public UpdateRuleResponse doHandle(UpdateRuleRequest request)
	{
		boolean result = false;

		if (request.getType() == TYPE.ADD)
			result = createRule(request.getRule());
		else if (request.getType() == TYPE.DELETE)
			result = deleteRule(request.getRule());
		else if (request.getType() == TYPE.EDIT)
			result = false;

		return new UpdateRuleResponse(request.getRule().getId(), result);
	}

	@Override
	public UpdateRuleRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeConverter());
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, UpdateRuleRequest.class);
	}

	boolean deleteRule(Rule rule)
	{
		try
		{
			jdbcTemplate.update("DELETE FROM rule WHERE ruleId=?", rule.getId());
			jdbcTemplate.update("DELETE FROM rule_then WHERE ruleId=?", rule.getId());
			jdbcTemplate.update("DELETE FROM rule_condition WHERE ruleId=?", rule.getId());

			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not delete rule", ex);
			return false;
		}
	}

	boolean createRule(Rule rule)
	{
		int ruleId = insertNewRule(rule);

		if (ruleId == 0)
			return false;

		rule.setId(ruleId);
		for (RuleThen ruleThen : rule.getThenList())
		{
			insertRuleThen(ruleId, ruleThen);
		}

		for (RuleCondition ruleCondition : rule.getConditionList())
		{
			insertRuleCondition(ruleId, ruleCondition);
		}

		return true;
	}

	int insertNewRule(Rule rule)
	{
		try
		{
			SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleInsert.withTableName("rule");
			simpleInsert.setGeneratedKeyName("RuleId");

			DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("Name", rule.getName());
			parameters.put("Active", String.valueOf(rule.isActive()));
			parameters.put("WhenDeviceId", rule.getWhenDeviceId());
			parameters.put("WhenAction", rule.getWhenAction().toString());
			parameters.put("WhenTime", rule.getWhenTime().toString(fmt));

			Number id = simpleInsert.executeAndReturnKey(parameters);
			return id.intValue();
		}
		catch (Exception ex)
		{
			logger.error("Could not add rule", ex);
			return 0;
		}
	}

	boolean insertRuleThen(int ruleId, RuleThen ruleThen)
	{
		try
		{
			SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleInsert.withTableName("rule_then");
			simpleInsert.setGeneratedKeyName("Id");

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("RuleId", ruleId);
			parameters.put("Action", ruleThen.getAction());
			parameters.put("DeviceId", ruleThen.getDeviceId());
			parameters.put("DimLevel", ruleThen.getDimLevel());

			simpleInsert.execute(parameters);

			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not insert rule then", ex);
			return false;
		}
	}

	boolean insertRuleCondition(int ruleId, RuleCondition ruleCondition)
	{
		try
		{
			SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleInsert.withTableName("rule_condition");
			simpleInsert.setGeneratedKeyName("Id");

			DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("RuleId", ruleId);
			parameters.put("ConditionTrigger", ruleCondition.getCondition().toString());
			parameters.put("TimeOfDay", ruleCondition.getTimeOfDay().toString(fmt));
			parameters.put("DayOfWeek", ruleCondition.getDayOfWeek());
			parameters.put("DelayInMinutes", ruleCondition.getDelayInMinutes());
			parameters.put("FromTime", ruleCondition.getFromTime().toString(fmt));
			parameters.put("ToTime", ruleCondition.getToTime().toString(fmt));
			parameters.put("FromDayOfWeek", ruleCondition.getFromDayOfWeek());
			parameters.put("ToDayOfWeek", ruleCondition.getToDayOfWeek());

			simpleInsert.execute(parameters);

			return true;
		}
		catch (Exception ex)
		{
			logger.error("Could not insert rule condition", ex);
			return false;
		}
	}
}
