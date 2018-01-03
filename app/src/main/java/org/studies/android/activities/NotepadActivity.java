package org.studies.android.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.studies.android.R;

import java.io.File;

public class NotepadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView filePath = (TextView) findViewById(R.id.file_name);
                TextView fileContent = (TextView) findViewById(R.id.file_content);

                try {
                    File file = new File(filePath.getText().toString());
                    file.createNewFile();
                } catch (Exception e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }




            }
        });
    }

}
