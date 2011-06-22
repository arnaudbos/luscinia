package uk.ac.brookes.arnaudbos.luscinia.utils;

import org.ektorp.CouchDbConnector;

public interface ICouchDBUtils
{
	public CouchDbConnector getDB (String login, String password);
}
