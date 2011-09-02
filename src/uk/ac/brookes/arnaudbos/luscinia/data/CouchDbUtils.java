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

import java.util.List;

import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;

public class CouchDbUtils implements ICouchDbUtils
{
	private static final String DBNAME = "luscinia";
	private static final String VIEWS_DESIGN_DOCUMENT = "_design/views";
	private static final String REMOTE_SERVER = "sneakernet.iriscouch.com";
	private static String HOST ;
	private static int PORT;
	private static CouchDbInstance dbInstance ;
	private static CouchDbConnector dbConnector ;

	public void setHost (String host, int port)
	{
		HOST = host;
		PORT = port;
	}
	
	public void connect (String login, String password) throws Exception
	{
		if(HOST==null)
		{
			throw new Exception ("Local instance error!");
		}

		HttpClient httpClient = new StdHttpClient.Builder().host(REMOTE_SERVER).port(5984).username(login).password(password).build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector dbConnector = dbInstance.createConnector(DBNAME, true);

//		ReplicationCommand cmd = ReplicationCommand.Builder()
//						.source("https://"+login+":"+password+"@"+REMOTE_SERVER+"/_users")
//						.target("_users")
//						.build();
//		ReplicationStatus status = dbInstance.replicate(cmd);
//
//		HttpClient authenticatedHttpClient = new StdHttpClient.Builder().host(HOST).port(PORT).username(login).password(password).build();
//		this.dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
//		this.dbConnector = this.dbInstance.createConnector(DBNAME, true);
//
//		ReplicationCommand cmd = ReplicationCommand.Builder()
//						.source("https://"+login+":"+password+"@"+REMOTE_SERVER+"/"+DBNAME)
//						.target(DBNAME)
//						.build();
//		ReplicationStatus status = dbInstance.replicate(cmd);
	}

	@Override
	public <T> List<T> queryView (String viewName, Class<T> type)
	{
		ViewQuery viewQuery = new ViewQuery().designDocId(VIEWS_DESIGN_DOCUMENT).viewName(viewName);
		return dbConnector.queryView(viewQuery, type);
	}

	@Override
	public <T> List<T> queryView (String viewName, Class<T> type, String key)
	{
		ViewQuery viewQuery = new ViewQuery().designDocId(VIEWS_DESIGN_DOCUMENT).viewName(viewName).key(key);
		return dbConnector.queryView(viewQuery, type);
	}

	@Override
	public void create (Object document)
	{
		dbConnector.create(document);
	}

	@Override
	public void update (Object document)
	{
		dbConnector.update(document);
	}

	@Override
	public void delete (Object document)
	{
		dbConnector.delete(document);
	}

	@Override
	public <T> T get (Class<T> c, String id, String rev)
	{
		return dbConnector.get(c, id, rev);
	}

	@Override
	public AttachmentInputStream getAttachment(String docId, String attachmentId)
	{
		return dbConnector.getAttachment(docId, attachmentId);
	}
}
