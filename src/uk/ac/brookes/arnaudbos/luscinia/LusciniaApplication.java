/**
 * 
 */
package uk.ac.brookes.arnaudbos.luscinia;

import java.util.List;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

/**
 * @author arnaud
 *
 */
public class LusciniaApplication extends RoboApplication
{
    @Override
    protected void addApplicationModules(List<Module> modules)
    {
        modules.add(new LusciniaModule());
    }
}