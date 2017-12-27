package org.studies.android.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.studies.android.R;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SearchFilesActivity extends AppCompatActivity {

    Searcher searcher;

    TextView searchResultText;

    void searchDone() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progress);
        progressBar.setVisibility(View.INVISIBLE);
    }

    void setResult(List<String> result) {
        Log.i(this.getClass().getName(), "!!! Result found " + result);
        String resultString = "";
        for (String path : result) {
            resultString = resultString + "\n\r" + path;
        }
        searchResultText.setText(resultString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_files);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progress);

        final EditText searchText = (EditText)  findViewById(R.id.pattern);

        searchResultText = (TextView) findViewById(R.id.search_result);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String pattern = searchText.getText().toString();
                Log.i(this.getClass().getName(), "Looking for " + pattern);
                searcher = new Searcher(pattern, SearchFilesActivity.this);
                runOnUiThread(searcher);
            }
        });




    }



    class Searcher extends Thread {

        List<String> result;
        File currentDir;
        String pattern;
        SearchFilesActivity callback;

        public Searcher(String str, SearchFilesActivity aCallback) {
            try {
                result = new ArrayList<String>();
                currentDir =  new File(new URI("file:/"));
                pattern = str;
                callback = aCallback;
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
        }

        @Override
        public void run() {
            scan(currentDir);
            callback.searchDone();
        }

        private void scan(File dirToScan) {
            Log.d(this.getClass().getName(), "Scanning " + dirToScan);
            if (dirToScan.getAbsolutePath().startsWith("/sys")||dirToScan.getAbsolutePath().startsWith("/proc")) {
                Log.d(this.getClass().getName(), dirToScan + " nothing to search here");
                return;
            }
            if (!dirToScan.canRead()) {
                Log.d(this.getClass().getName(), "Can not read " + dirToScan);
                return;
            }
            for (File file : dirToScan.listFiles()) {
                if (file.getName().contains(pattern)){
                    Log.d(this.getClass().getName(), "File " + file.getName() + " matches pattern " + pattern);
                    result.add(file.getPath());
                    callback.setResult(result);
                }
                if (file.isDirectory()) {
                    scan(file);
                }
            }
        }

    }

}

//interface Callback {
//
//    public void setResult(List<String> result);
//
//    public void searchDone();
//
//}
