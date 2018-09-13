package org.studies.android.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.studies.android.R;

public class PageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        GridView gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(new MyGridAdapter());
    }

    class MyGridAdapter extends BaseAdapter {

        Bitmap[] bitmaps;

        public MyGridAdapter() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmaps = new Bitmap[6];
            bitmaps[0] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            bitmaps[1] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            bitmaps[2] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            bitmaps[3] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            bitmaps[4] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            bitmaps[5] = BitmapFactory
                    .decodeStream((getResources().openRawResource(R.raw.sample_file_vertical)), null, options);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return bitmaps.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view = new ImageView(PageActivity.this.getApplicationContext());
            view.setImageBitmap(bitmaps[position]);
            return view;
        }
    }

}
