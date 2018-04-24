package org.studies.android.activities;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.studies.android.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Shooter shooter = new Shooter();
        shooter.start();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e(Camera.class.getName(), "Failed to create a camera", e);
        }
        return c; // returns null if camera is unavailable
    }

    class ShotGrabber implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    }

    class Shooter extends Thread {

        Camera camera;
        HttpURLConnection urlConnection;

        public Shooter() {
            camera = getCameraInstance();
            camera.startPreview();
        }

        @Override
        public void run() {

            try {
                URL url = new URL("http://192.168.137.1:8080/beholder-master/post");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
            } catch (Exception e) {
                Log.i(getClass().getName(), "Connection not created", e);
            }

            while(true) {
                try {
                    Thread.currentThread().sleep(10000);

                    camera.takePicture(
                        null,
                            new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] data, Camera camera) {
                                    Log.i(getClass().getName(), "Raw taken");
                                }
                            },null,
                        new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                try {
                                    urlConnection.getOutputStream().write(data);
                                    urlConnection.getOutputStream().flush();
                                    Log.i(getClass().getName(), String.format("Shot taken. size is %d bytes", data.length));
                                } catch (IOException ee) {
                                    Log.i(getClass().getName(), "Shot not sent", ee);
                                }
                            }
                        });


//                                try {
//                                    urlConnection.getOutputStream().write("Hi there".getBytes());
//                                    urlConnection.getOutputStream().flush();
//                                    Log.i(getClass().getName(), String.format("Shot taken. size is %d bytes", "Hi there".getBytes().length));
//                                } catch (IOException ee) {
//                                    Log.i(getClass().getName(), "Shot not sent", ee);
//                                }



                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), "Pause failed", e);
                }
            }
        }
    }


}
