package org.studies.android;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowseFilesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final FileListAdapter fileListAdapter = new FileListAdapter();
        setListAdapter(fileListAdapter);

        ListView listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(this.getClass().getName(), i + " clicked");
                if (fileListAdapter.currentFiles.get(i).isFile()) {
                    Snackbar.make(view, "It is file", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (!fileListAdapter.currentFiles.get(i).canRead()) {
                    Snackbar.make(view, "You could not open this", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                fileListAdapter.setRoot(i);
            }
        });
    }

    class FileListAdapter extends BaseAdapter {

        private File rootDir;

        public File currentDirectory;
        List<File> currentFiles;

        public FileListAdapter() {
            super();
            try {
                rootDir = new File(new URI("file:/"));
                currentDirectory = rootDir;
                currentFiles = new ArrayList<File>();
                //currentFiles = new SortedList<File>(File.class, new SortCallback());
                for (File file : currentDirectory.listFiles()) {
                    currentFiles.add(file);
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
                e.printStackTrace();
            }
        }

        public void setRoot(int index) {
            Log.d(this.getClass().getName(), "setRoot");
            File newRoot =  currentFiles.get(index);
            if (newRoot.isDirectory()) {
                if (newRoot.canRead()) {
                    currentFiles.clear();
                    currentFiles.addAll(Arrays.asList(newRoot.listFiles()));
                    if (!rootDir.equals(newRoot))
                        currentFiles.add(0, newRoot.getParentFile());
                    currentDirectory = newRoot;
                    notifyDataSetChanged();
                    Log.i(this.getClass().getName(), "Changing root to " + newRoot.getAbsolutePath());
                } else {
                    Log.i(this.getClass().getName(), "Sorry, you can not read " + newRoot.getAbsolutePath());
                }
            } else {
                Log.i(this.getClass().getName(), newRoot.getAbsolutePath() + " is file");
            }
        }

        @Override
        public int getCount() {
            return currentFiles.size();
        }

        @Override
        public Object getItem(int i) {
            return currentFiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return currentFiles.get(i).hashCode();
        }

        @Override

        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.items_list, container, false);
            }
            TextView textView = (TextView)((ConstraintLayout)convertView).getChildAt(0);
            File selectedFile = currentFiles.get(position);
            textView.setText(selectedFile.getAbsolutePath());
            if (!selectedFile.canRead()) {
                textView.setTextColor(Color.MAGENTA);
            } else {
                textView.setTextColor(Color.BLACK);
            }
            if (selectedFile.isDirectory()) {
                textView.setTypeface(null, Typeface.BOLD);
            } else {
                textView.setTypeface(null, Typeface.NORMAL);
            }
            if (position == 0&&!currentDirectory.equals(rootDir)) {
                textView.setText("..");
            }
            return convertView;
        }

    }

}
