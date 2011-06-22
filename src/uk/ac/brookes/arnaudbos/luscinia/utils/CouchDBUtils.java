package uk.ac.brookes.arnaudbos.luscinia.utils;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDBUtils implements ICouchDBUtils
{
	private final static String HOST = "sneakernet.iriscouch.com";

	public CouchDbConnector getDB (String login, String password)
	{
		HttpClient authenticatedHttpClient = new StdHttpClient.Builder().host(HOST).port(5984).username(login).password(password).build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
		CouchDbConnector db = dbInstance.createConnector("luscinia", true);
		
		return db;
	}
}
