package uk.ac.brookes.arnaudbos.luscinia.utils;

/**
 * Mapper class used to determine the type of activity to launch from a folder type
 * @author arnaudbos
 */
public enum TemplateActivityMapper
{
	GENERIC, TRANS, MACROCIBLE, NURSING_DIAGRAM;
	
	public static TemplateActivityMapper toActivity(String folderType)
	{
		if(folderType==null || folderType.equals(""))
		{
			return GENERIC;
		}
		else
		{
			return valueOf(folderType);
		}
	}
}
