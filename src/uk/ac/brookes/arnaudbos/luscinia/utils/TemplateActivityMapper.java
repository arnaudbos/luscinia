package uk.ac.brookes.arnaudbos.luscinia.utils;

public enum TemplateActivityMapper
{
	GENERIC, TRANS, MACROCIBLE, NURSING_DIAGRAM;
	
	public static TemplateActivityMapper toActivity(String templateName)
	{
		if(templateName==null || templateName.equals(""))
		{
			return GENERIC;
		}
		else
		{
			return valueOf(templateName.substring(templateName.indexOf("-")+1).toUpperCase());
		}
	}
}
