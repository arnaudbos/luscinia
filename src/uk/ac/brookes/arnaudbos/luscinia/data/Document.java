/*
 * Copyright (C) 2011 Arnaud Bos <arnaud.tlse@gmail.com>
 * 
 * This file is part of Luscinia.
 * 
 * Luscinia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Luscinia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Luscinia.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

/**
 * Luscinia document object
 * @author arnaudbos
 */
public class Document extends CouchDbDocument
{
	private static final long serialVersionUID = 3185003212567128889L;

	public static final String DOCUMENT_TYPE = "luscinia/document";
	public static final String VIEW_ALL_DOCUMENTS = "_all_documents";

	private String docType;
	private String folderId;
	private String title;
	private String type;
	private Date date;
	private Map<String, Object> unknownFields = new HashMap<String, Object>();
	
	public Document()
	{
		setDate(new Date());
		setDocType(DOCUMENT_TYPE);
	}
	
	public Document(String id, String revision, Date date)
	{
		setId(id);
		setRevision(revision);
		setDate(date);
		setDocType(DOCUMENT_TYPE);
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
	 * @return the folder Id
	 */
	@JsonProperty("folder_id")
	public String getFolderId()
	{
		return folderId;
	}

	/**
	 * @param folderId The folder Id to set
	 */
	@JsonProperty("folder_id")
	public void setFolderId(String folderId)
	{
		this.folderId = folderId;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type The type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date The date to set
	 */
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

	/**
	 * @return the unknown fields
	 */
    @JsonAnyGetter
    public Map<String, Object> getUnknownFields()
    {
    	return unknownFields;
    }
}