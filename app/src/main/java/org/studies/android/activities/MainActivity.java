package org.studies.android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import android.widget.ListView;

import org.studies.android.ActivitiesListAdapter;
import org.studies.android.R;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActivitiesListAdapter activitiesListAdapter = new ActivitiesListAdapter(this);
        activitiesListAdapter.addLink("Browse Files", BrowseFilesActivity.class);
        activitiesListAdapter.addLink("Layout Samples", LayoutActivity.class);
        setListAdapter(activitiesListAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d(this.getClass().getName(), i + " clicked");
            Intent intentOne = new Intent(MainActivity.this, activitiesListAdapter.getActivityAt(i));
            startActivity(intentOne);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    class MainListAdapter extends ActivitiesListAdapter {
//
//        String[] links = {"File Browser"};
//
//        @Override
//        public int getCount() {
//            return links.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return links[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return links[position].hashCode();
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.items_list, container, false);
//            }
//            TextView textView = (TextView)((ConstraintLayout)convertView).getChildAt(0);
//            textView.setText(links[position]);
//            textView.setTypeface(null, Typeface.BOLD);
//            textView.setTextSize(36);
//            return convertView;
//        }
//
//    }


}


