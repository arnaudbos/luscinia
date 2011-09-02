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

package uk.ac.brookes.arnaudbos.luscinia.utils;

import java.util.List;

import org.ektorp.AttachmentInputStream;

public interface ICouchDbUtils
{
	public void setHost (String host, int port);

	public void connect (String login, String password) throws Exception;
	
	public <T> List<T> queryView (String viewName, Class<T> type);
	
	public <T> List<T> queryView (String viewName, Class<T> type, String key);
	
	public void create (Object document);
	
	public void update (Object document);
	
	public void delete (Object document);
	
	public <T> T get (Class<T> c, String id, String rev);
	
	public AttachmentInputStream getAttachment (String docId, String attachmentId);
}
