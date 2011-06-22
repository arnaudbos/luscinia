/**
 * 
 */
package uk.ac.brookes.arnaudbos.luscinia;

import roboguice.config.AbstractAndroidModule;
import uk.ac.brookes.arnaudbos.luscinia.utils.CouchDBUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDBUtils;

/**
 * @author arnaud
 *
 */
public class LusciniaModule extends AbstractAndroidModule
{
    @Override
    protected void configure()
    {
        // core stuff
        bind(ICouchDBUtils.class).to(CouchDBUtils.class);
 
        // @Inject SharedPreferences
        //bindConstant().annotatedWith(SharedPreferencesName.class).to("org.uk.brookes.arnaudbos");
    }
}