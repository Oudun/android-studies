package org.studies.android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.studies.android.ActivitiesListAdapter;
import org.studies.android.R;

public class LayoutActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final ActivitiesListAdapter activitiesListAdapter = new ActivitiesListAdapter(this);
        activitiesListAdapter.addLink("Linear Layout", LinearLayoutActivity.class);
        activitiesListAdapter.addLink("Relative Layout", RelativeLayoutActivity.class);
        activitiesListAdapter.addLink("Grid Layout", GridLayoutActivity.class);




//        more layouts to check:
//        AbsoluteLayout,AdapterView<T extends Adapter>,FragmentBreadCrumbs,FrameLayout,GridLayout,LinearLayout,RelativeLayout,SlidingDrawer,Toolbar,TvView


        setListAdapter(activitiesListAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(this.getClass().getName(), i + " clicked");
                Intent intentOne = new Intent(LayoutActivity.this, activitiesListAdapter.getActivityAt(i));
                startActivity(intentOne);
            }
        });

        FloatingActionButton home = (FloatingActionButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LayoutActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}
