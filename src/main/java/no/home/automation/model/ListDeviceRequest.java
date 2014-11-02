package no.home.automation.model;

public class ListDeviceRequest extends RequestValidator
{
	private String	category	= null;

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	@Override
	public void validateRequest() throws IllegalArgumentException
	{

	}
}
