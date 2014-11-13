package no.home.automation.model;

public class UpdateRuleRequest extends RequestValidator
{
	public enum TYPE
	{
		ADD, DELETE, EDIT
	};

	private TYPE	type;
	private Rule	rule;

	public UpdateRuleRequest(TYPE type, Rule rule)
	{
		super();
		this.type = type;
		this.rule = rule;
	}

	public TYPE getType()
	{
		return type;
	}

	public void setType(TYPE type)
	{
		this.type = type;
	}

	public Rule getRule()
	{
		return rule;
	}

	public void setRule(Rule rule)
	{
		this.rule = rule;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{
		if (rule.getWhenDeviceId() == 0 && type != TYPE.DELETE)
			throw new IllegalArgumentException("whenDeviceId is missing");
		if (type != TYPE.DELETE && (rule.getThenList() == null || rule.getThenList().size() == 0))
			throw new IllegalArgumentException("then list is empty");
		if (type != TYPE.DELETE && (rule.getConditionList() == null || rule.getConditionList().size() == 0))
			throw new IllegalArgumentException("condition list is empty");

		if (type != TYPE.DELETE)
		{
			for (RuleThen rule : rule.getThenList())
				rule.validate();

			for (RuleCondition rule : rule.getConditionList())
				rule.validate();
		}
	}
}
