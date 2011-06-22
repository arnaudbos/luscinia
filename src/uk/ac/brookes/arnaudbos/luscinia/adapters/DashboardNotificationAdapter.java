package uk.ac.brookes.arnaudbos.luscinia.adapters;

import java.util.List;

import uk.ac.brookes.arnaudbos.luscinia.data.Notification;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DashboardNotificationAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private List<String> groups;
	private List<List<Notification>> notifications;

	public DashboardNotificationAdapter(Context context, List<String> groups, List<List<Notification>> notifications)
	{
		this.context = context;
		this.groups = groups;
		this.notifications = notifications;
	}
	
	public Notification getChild(int groupPosition, int childPosition)
	{
		return notifications.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	public int getChildrenCount(int groupPosition)
	{
		return notifications.get(groupPosition).size();
	}

	public TextView getGenericView()
	{
        final float scale = this.context.getResources().getDisplayMetrics().density;
        
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int)(64 * scale + 0.5f));

		TextView textView = new TextView(context);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding((int)(36 * scale + 0.5f), 0, 0, 0);
		textView.setLayoutParams(lp);
		textView.setHeight(48);
		return textView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		TextView textView = getGenericView();
		textView.setText(getChild(groupPosition, childPosition).getName());
		return textView;
	}

	public Object getGroup(int groupPosition)
	{
		return groups.get(groupPosition);
	}

	public int getGroupCount()
	{
		return groups.size();
	}

	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		TextView textView = getGenericView();
		textView.setText(getGroup(groupPosition).toString());
		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	public boolean hasStableIds()
	{
		return true;
	}
}