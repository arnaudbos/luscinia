/**
 * 
 */
package uk.ac.brookes.arnaudbos.luscinia;

import java.util.List;

import org.ektorp.CouchDbConnector;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

/**
 * @author arnaudbos
 */
public class LusciniaApplication extends RoboApplication
{
	private static CouchDbConnector _db; 

	@Override
    protected void addApplicationModules(List<Module> modules)
    {
        modules.add(new LusciniaModule());
    }

	/**
	 * @return the CouchDbConnector object used as session
	 */	
	public static CouchDbConnector getDB()
	{
		return _db;
	}

	/**
	 * @param db the CouchDbConnector to set as session
	 */	
	public static void setDB(CouchDbConnector db)
	{
		_db = db;
	}
}