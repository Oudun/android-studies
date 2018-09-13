package org.studies.android.activities;

import android.app.Activity;
import android.os.Bundle;
import org.studies.android.R;

public class CustomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        getCurrentFocus().invalidate();
    }
}
