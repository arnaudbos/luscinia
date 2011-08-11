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

package uk.ac.brookes.arnaudbos.luscinia.widget;

/**
 * 
 * Implementation of a basic ScrollView synchronizer, see also LusciniaScollView and NursingDiagramListener.onScrollChanged().
 * Thanks to @Andy on StackOverflow question:
 * http://stackoverflow.com/questions/3948934/synchronise-scrollview-scroll-positions-android
 *
 */
public interface ScrollViewListener
{
	void onScrollChanged(LusciniaScrollView scrollView, int x, int y, int oldx, int oldy);
}
