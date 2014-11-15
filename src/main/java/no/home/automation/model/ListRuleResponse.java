package no.home.automation.model;

import java.util.List;

public class ListRuleResponse extends DefaultReponse
{
	private List<Rule>	rules;

	public ListRuleResponse(List<Rule> rules)
	{
		this.rules = rules;
	}

	public List<Rule> getRules()
	{
		return rules;
	}

	public void setRules(List<Rule> rules)
	{
		this.rules = rules;
	}
}
