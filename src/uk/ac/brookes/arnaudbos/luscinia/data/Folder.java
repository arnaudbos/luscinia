package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

public class Folder extends CouchDbDocument
{
	public static final String FOLDER_TYPE = "http://www.sneakernet.fr/luscinia/folder";
	public static final String VIEW_ALL_FOLDERS = "_all_folders";
	public static final int ADMINISTRATIVE_FOLDER_TYPE = 0;
	public static final int NURSING_FOLDER_TYPE = 1;
	public static final int MEDICAL_FOLDER_TYPE = 2;

	private String docType;
	private String patientId;
	private String title;
	private int type;
	private Date date;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
	public Folder()
	{
		setDate(new Date());
		setDocType(FOLDER_TYPE);
	}
	
	public Folder(String id, String revision, Date date)
	{
		setId(id);
		setRevision(revision);
		setDate(date);
		setDocType(FOLDER_TYPE);
	}

	@JsonProperty("docType")
	public String getDocType()
	{
		return docType;
	}
	
	@JsonProperty("docType")
	private void setDocType(String docType)
	{
		this.docType = docType;
	}

	@JsonProperty("patient_id")
	public String getPatientId()
	{
		return patientId;
	}
	
	@JsonProperty("patient_id")
	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public Date getDate()
	{
		return date;
	}

	private void setDate(Date date)
	{
		this.date = date;
	}

	@JsonAnySetter
    public void add(String key, Object value)
    {
		if(unknownFields.containsKey(key))
		{
			if(key.contains("#"))
			{
				int count = Integer.valueOf(key.substring(key.lastIndexOf("#")+1))+1;
				String newKey = key.substring(0, key.lastIndexOf("#")+1) + count;
				add(newKey, value);
			}
			else
			{
				String newKey = key + "#2";
				add(newKey, value);
			}
		}
		else if (key.equals("firstname") || key.equals("lastname") || key.equals("date_of_birth") || key.equals("insee") ||
				key.equals("telephone") || key.equals("weight") || key.equals("size") || key.equals("docType"))
		{
			String newKey = key + "#2";
			add(newKey, value);
		}
		else
		{
			unknownFields.put(key, value);
		}
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties()
    {
    	return unknownFields;
    }
}