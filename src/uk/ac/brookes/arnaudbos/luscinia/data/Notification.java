package uk.ac.brookes.arnaudbos.luscinia.data;

public class Notification
{
	private String name;
	private String message;
	
	public Notification (String name, String message)
	{
		this.name = name;
		this.message = message;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
