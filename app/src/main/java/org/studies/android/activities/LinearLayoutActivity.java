package org.studies.android.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.studies.android.R;

import static android.widget.LinearLayout.VERTICAL;

public class LinearLayoutActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.hor_layout: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                break;
            }
            case R.id.ver_layout: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                break;
            }
            case R.id.bottom_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.BOTTOM);
                break;
            }
            case R.id.center_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.CENTER);
                break;
            }

            case R.id.clip_horizontal_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.CLIP_HORIZONTAL);
                break;
            }
            case R.id.clip_vertical_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.CLIP_VERTICAL);
                break;
            }
            case R.id.displpay_clip_horizontal_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.DISPLAY_CLIP_HORIZONTAL);
                break;
            }
            case R.id.display_clip_vertical_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.DISPLAY_CLIP_VERTICAL);
                break;
            }
            case R.id.end_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.END);
                break;
            }
            case R.id.fill_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.FILL);
                break;
            }
            case R.id.fill_horizontal_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.FILL_HORIZONTAL);
                break;
            }
            case R.id.fill_vertical_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.FILL_VERTICAL);
                break;
            }
            case R.id.horizontal_gravity_mask_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                break;
            }
            case R.id.left_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.LEFT);
                break;
            }
            case R.id.no_gravity_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.NO_GRAVITY);
                break;
            }
            case R.id.relative_horizontal_gravity_mask_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                break;
            }
            case R.id.right_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.RIGHT);
                break;
            }
            case R.id.top_gravity: {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
                linearLayout.setGravity(Gravity.TOP);
                break;
            }













//            BOTTOM
//                    CENTER
//            CENTER_HORIZONTAL
//                    CENTER_VERTICAL
//            CLIP_HORIZONTAL
//                    CLIP_VERTICAL
//            DISPLAY_CLIP_HORIZONTAL
//                    DISPLAY_CLIP_VERTICAL
//            END
//                    FILL
//            FILL_HORIZONTAL
//                    FILL_VERTICAL
//            HORIZONTAL_GRAVITY_MASK
//                    LEFT
//            NO_GRAVITY
//                    RELATIVE_HORIZONTAL_GRAVITY_MASK
//            RELATIVE_LAYOUT_DIRECTION
//                    RIGHT
//            START
//                    TOP
//            VERTICAL_GRAVITY_MASK
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
