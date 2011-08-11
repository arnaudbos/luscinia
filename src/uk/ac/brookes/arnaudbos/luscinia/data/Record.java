package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

public class Record extends CouchDbDocument
{
	public static final String RECORD_TYPE = "http://www.sneakernet.fr/luscinia/record";
	public static final String VIEW_ALL_RECORDS = "_all_records";

	private String docType;
	private String documentId;
	private Date date;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
	public Record()
	{
		setDate(new Date());
		setDocType(RECORD_TYPE);
	}
	
	public Record(String id, String revision, Date date)
	{
		setId(id);
		setRevision(revision);
		setDate(date);
		setDocType(RECORD_TYPE);
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

	@JsonProperty("document_id")
	public String getDocumentId()
	{
		return documentId;
	}
	
	@JsonProperty("document_id")
	public void setDocumentId(String documentId)
	{
		this.documentId = documentId;
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