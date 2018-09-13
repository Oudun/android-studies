package org.studies.android.activities;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.studies.android.R;

import java.util.ArrayList;
import java.util.List;

public class CanvasActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        setListAdapter(new CanvasListAdapter());
    }

    class CanvasListAdapter extends BaseAdapter {

        List<Integer> list;

        public CanvasListAdapter() {
            list = new ArrayList<Integer>();
            list.add(Color.GRAY);
            list.add(Color.BLUE);
            list.add(Color.RED);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.canvas, parent, false);
            }
            SurfaceView view = null;
            if (convertView instanceof ConstraintLayout) {
                ConstraintLayout constraintLayout = (ConstraintLayout)convertView;
                view = (SurfaceView)(constraintLayout.getChildAt(0));
            } else {
                view = (SurfaceView)convertView;
            }
            //SurfaceView view = (SurfaceView)convertView;
            view.setBackgroundColor(list.get(position));
            view.setMinimumHeight(4000);
            view.setX(20);
            return view;
        }

    }

}
