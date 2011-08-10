package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

public class Document extends CouchDbDocument
{
	public static final String DOCUMENT_TYPE = "http://www.sneakernet.fr/luscinia/document";
	public static final String VIEW_ALL_DOCUMENTS = "_all_documents";

	private String docType;
	private String folderId;
	private String title;
	private String type;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
	public Document()
	{}
	
	public Document(String id, String revision)
	{
		setId(id);
		setRevision(revision);
	}

	@JsonProperty("docType")
	public String getDocType()
	{
		return docType;
	}
	
	@JsonProperty("docType")
	public void setDocType(String docType)
	{
		this.docType = docType;
	}

	@JsonProperty("folder_id")
	public String getFolderId()
	{
		return folderId;
	}
	
	@JsonProperty("folder_id")
	public void setFolderId(String folderId)
	{
		this.folderId = folderId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
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