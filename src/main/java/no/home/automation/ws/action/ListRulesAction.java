package no.home.automation.ws.action;

import java.util.List;

import no.home.automation.dao.RuleConditionRowMapper;
import no.home.automation.dao.RuleRowMapper;
import no.home.automation.dao.RuleThenRowMapper;
import no.home.automation.model.DefaultHandler;
import no.home.automation.model.ListRuleRequest;
import no.home.automation.model.ListRuleResponse;
import no.home.automation.model.Rule;
import no.home.automation.model.RuleCondition;
import no.home.automation.model.RuleThen;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ListRulesAction extends DefaultHandler<ListRuleRequest, ListRuleResponse>
{
	private JdbcTemplate	jdbcTemplate	= null;

	public ListRulesAction(boolean mustBeAuthenticated, JdbcTemplate jdbcTemplate)
	{
		super(mustBeAuthenticated);
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ListRuleResponse doHandle(ListRuleRequest request)
	{
		return new ListRuleResponse(getAllRules());
	}

	@Override
	public ListRuleRequest translate(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.fromJson(json, ListRuleRequest.class);
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
