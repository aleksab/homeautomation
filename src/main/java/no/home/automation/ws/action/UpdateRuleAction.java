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
import no.home.automation.service.RuleEngine;
import no.home.automation.ws.LocalTimeTypeConverter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UpdateRuleAction extends DefaultHandler<UpdateRuleRequest, UpdateRuleResponse>
{
	private DataSourceTransactionManager	txManager	= null;
	private RuleEngine						engine		= null;

	public UpdateRuleAction(boolean mustBeAuthenticated, RuleEngine engine, DataSourceTransactionManager txManager)
	{
		super(mustBeAuthenticated);

		this.engine = engine;
		this.txManager = txManager;
	}

	@Override
	public UpdateRuleResponse doHandle(UpdateRuleRequest request)
	{
		boolean result = false;

		if (request.getType() == TYPE.ADD)
		{
			result = createRule(request.getRule());
			engine.reloadEngine();
		}
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
			JdbcTemplate jdbcTemplate = new JdbcTemplate(txManager.getDataSource());
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
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

		try
		{
			insertNewRule(rule);

			for (RuleThen ruleThen : rule.getThenList())
			{
				insertRuleThen(rule.getId(), ruleThen);
			}

			for (RuleCondition ruleCondition : rule.getConditionList())
			{
				insertRuleCondition(rule.getId(), ruleCondition);
			}

			txManager.commit(status);
			return true;
		}
		catch (Exception ex)
		{
			txManager.rollback(status);
			logger.error("Could not create rule", ex);
			return false;
		}
	}

	void insertNewRule(Rule rule)
	{
		SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(txManager.getDataSource());
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
		rule.setId(id.intValue());
	}

	void insertRuleThen(int ruleId, RuleThen ruleThen)
	{

		SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(txManager.getDataSource());
		simpleInsert.withTableName("rule_then");
		simpleInsert.setGeneratedKeyName("Id");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("RuleId", ruleId);
		parameters.put("Action", ruleThen.getAction());
		parameters.put("DeviceId", ruleThen.getDeviceId());
		parameters.put("Value", ruleThen.getValue());

		simpleInsert.execute(parameters);
	}

	void insertRuleCondition(int ruleId, RuleCondition ruleCondition)
	{
		SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(txManager.getDataSource());
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
	}
}
