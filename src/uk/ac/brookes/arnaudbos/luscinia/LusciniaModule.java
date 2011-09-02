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

package uk.ac.brookes.arnaudbos.luscinia;

import roboguice.config.AbstractAndroidModule;
import roboguice.inject.SharedPreferencesName;
import uk.ac.brookes.arnaudbos.luscinia.data.CouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.ICouchDbUtils;
import uk.ac.brookes.arnaudbos.luscinia.utils.PreferencesNameProvider;

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
        bind(ICouchDbUtils.class).to(CouchDbUtils.class);
 
        // @Inject SharedPreferences
        bind(String.class).annotatedWith(SharedPreferencesName.class).toProvider(PreferencesNameProvider.class);
    }
}