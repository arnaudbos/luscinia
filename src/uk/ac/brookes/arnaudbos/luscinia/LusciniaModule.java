/**
 * 
 */
package uk.ac.brookes.arnaudbos.luscinia;

import roboguice.config.AbstractAndroidModule;

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
        //bind(... .class).to(...Impl.class);
 
        // @Inject SharedPreferences
        //bindConstant().annotatedWith(SharedPreferencesName.class).to("org.uk.brookes.arnaudbos");
    }
}