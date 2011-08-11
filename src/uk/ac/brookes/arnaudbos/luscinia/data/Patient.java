package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

/**
 * Luscinia patient object
 * @author arnaudbos
 */
public class Patient extends CouchDbDocument
{
	private static final long serialVersionUID = -6066162266461288656L;

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
		setDocType(PATIENT_TYPE);
	}
	
	public Patient(String id, String revision, Date dateOfCreation)
	{
		setId(id);
		setRevision(revision);
		setDateOfCreation(dateOfCreation);
		setDocType(PATIENT_TYPE);
	}

	/**
	 * @return the document type
	 */
	@JsonProperty("docType")
	public String getDocType()
	{
		return docType;
	}

	/**
	 * @param docType The document type to set
	 */
	@JsonProperty("docType")
	private void setDocType(String docType)
	{
		this.docType = docType;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname()
	{
		return firstname;
	}

	/**
	 * @param firstname The firstname to set
	 */
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname()
	{
		return lastname;
	}

	/**
	 * @param lastname The lastname to set
	 */
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	/**
	 * @return the date of creation
	 */
	@JsonProperty("date_of_creation")
	public Date getDateOfCreation()
	{
		return dateOfCreation;
	}

	/**
	 * @param dateOfCreation The date of creation to set
	 */
	@JsonProperty("date_of_creation")
	private void setDateOfCreation(Date dateOfCreation)
	{
		this.dateOfCreation = dateOfCreation;
	}

	/**
	 * @return the date of birth
	 */
	@JsonProperty("date_of_birth")
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth The date of birth to set
	 */
	@JsonProperty("date_of_birth")
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the insee number
	 */
	public String getInsee()
	{
		return insee;
	}

	/**
	 * @param insee The insee to set
	 */
	public void setInsee(String insee)
	{
		this.insee = insee;
	}

	/**
	 * @return the telephone number
	 */
	public String getTelephone()
	{
		return telephone;
	}

	/**
	 * @param telephone The telephone to set
	 */
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	/**
	 * @return the weight
	 */
	public Double getWeight()
	{
		return weight;
	}

	/**
	 * @param weight The weight to set
	 */
	public void setWeight(Double weight)
	{
		this.weight = weight;
	}

	/**
	 * @return the size
	 */
	public Double getSize()
	{
		return size;
	}

	/**
	 * @param size The size to set
	 */
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

	/**
	 * @return the unknown fields
	 */
    @JsonAnyGetter
    public Map<String, Object> getUnknownFields()
    {
    	return unknownFields;
    }
}