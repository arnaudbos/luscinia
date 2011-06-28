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
	private String firstname;
	private String lastname;
	private Date dateOfBirth;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
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

    @JsonAnySetter
    public void add(String key, Object value)
    {
    	unknownFields.put(key, value);
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