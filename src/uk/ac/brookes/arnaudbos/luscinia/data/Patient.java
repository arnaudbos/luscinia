package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

public class Patient extends CouchDbDocument
{
	public static final String PATIENT_TYPE = "http://www.sneakernet.fr/luscinia/patient";
	public static final String VIEW_ALL_PATIENTS = "_all_patients";

	private String docType;
	private String firstname;
	private String lastname;
	private Date dateOfCreation;
	private Date dateOfBirth;
	private String insee;
	private String telephone;
	private Double weight;
	private Double size;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
	public Patient()
	{
		setDateOfCreation(new Date());
	}
	
	public Patient(String id, String revision, Date dateOfCreation)
	{
		setId(id);
		setRevision(revision);
		setDateOfCreation(dateOfCreation);
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

	public String getFirstname()
	{
		return firstname;
	}
	
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	
	public String getLastname()
	{
		return lastname;
	}
	
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	@JsonProperty("date_of_creation")
	public Date getDateOfCreation()
	{
		return dateOfCreation;
	}

	@JsonProperty("date_of_creation")
	public void setDateOfCreation(Date dateOfCreation)
	{
		this.dateOfCreation = dateOfCreation;
	}

	@JsonProperty("date_of_birth")
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	@JsonProperty("date_of_birth")
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getInsee()
	{
		return insee;
	}

	public void setInsee(String insee)
	{
		this.insee = insee;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public Double getWeight()
	{
		return weight;
	}

	public void setWeight(Double weight)
	{
		this.weight = weight;
	}

	public Double getSize()
	{
		return size;
	}

	public void setSize(Double size)
	{
		this.size = size;
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
    
    @Override
    public String toString()
    {
    	return super.toString()+": lastname="+lastname+", firstname="+firstname+", date of birth="+dateOfBirth+", unknownFields="+unknownFields;
    }
}