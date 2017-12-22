package org.studies.android;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.studies.android.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddreval on 22.12.2017.
 */

public class ActivitiesListAdapter extends BaseAdapter {

    Map<String, Class> links;
    Activity activity;

    public ActivitiesListAdapter(Activity activity) {
        this.activity = activity;
        links = new HashMap<String, Class>();
    }

    public void addLink(String name, Class klass) {
        links.put(name, klass);
        notifyDataSetChanged();
    }

    public Class getActivityAt(int position) {
        return links.values().toArray(new Class[0])[position];
    }

    public String getActivityNameAt(int position) {
        return links.keySet().toArray(new String[0])[position];
    }

    @Override
    public int getCount() {
        return links.keySet().size();
    }

    @Override
    public Object getItem(int position) {
        return links.keySet().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return links.keySet().toArray()[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            //convertView = getLayoutInflater().inflate(R.layout.items_list, container, false);
            convertView = activity.getLayoutInflater().inflate(R.layout.items_list, container, false);
        }
        TextView textView = (TextView)((ConstraintLayout)convertView).getChildAt(0);
        textView.setText(links.keySet().toArray(new String[0])[position]);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(36);
        return convertView;
    }

}
