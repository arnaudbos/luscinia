package uk.ac.brookes.arnaudbos.luscinia.data;

import java.util.Date;

/**
 * Luscinia transmission record object
 * @author arnaudbos
 */
public class TransRecord extends Record
{
	private static final long serialVersionUID = 6808550110741432945L;

	private String focus;
	private String data;
	private String actions;
	private String results;
	
	public TransRecord()
	{
		super();
	}
	
	public TransRecord(String id, String revision, Date date)
	{
		super(id, revision, date);
	}

	/**
	 * @return the focus
	 */
	public String getFocus() {
		return focus;
	}

	/**
	 * @param focus the focus to set
	 */
	public void setFocus(String focus) {
		this.focus = focus;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the actions
	 */
	public String getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(String actions) {
		this.actions = actions;
	}

	/**
	 * @return the results
	 */
	public String getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(String results) {
		this.results = results;
	}
}