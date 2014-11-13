package no.home.automation.model;

public class UpdateRuleResponse extends DefaultReponse
{
	private int	ruleId;

	public UpdateRuleResponse(int ruleId, boolean result)
	{
		super(result);
		this.ruleId = ruleId;
	}

	public int getRuleId()
	{
		return ruleId;
	}

	public void setRuleId(int ruleId)
	{
		this.ruleId = ruleId;
	}
}
