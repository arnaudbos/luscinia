/**
 * 
 */
package uk.ac.brookes.arnaudbos.luscinia;

import java.util.List;

import org.ektorp.CouchDbConnector;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

/**
 * @author arnaud
 *
 */
public class LusciniaApplication extends RoboApplication
{
	private static CouchDbConnector _db; 

	@Override
    protected void addApplicationModules(List<Module> modules)
    {
        modules.add(new LusciniaModule());
    }
	
	public static CouchDbConnector getDB()
	{
		return _db;
	}
	
	public static void setDB(CouchDbConnector db)
	{
		_db = db;
	}
}