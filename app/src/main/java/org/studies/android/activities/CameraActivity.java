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
//            camera = getCameraInstance();
//            camera.startPreview();
        }

        @Override
        public void run() {
            doGet();
        }

        public void doGet() {
            try {
                URL url = new URL("http://192.168.137.1:8080/beholder-master/list");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                byte[] data = new byte[100];
                while(true) {
                    Thread.currentThread().sleep(10000);
                    int bytesRead = urlConnection.getInputStream().read(data);
                    Log.i(getClass().getName(), String.format("Post result is %s bytes received %d", new String(data), bytesRead));
                }
            } catch (Exception e) {
                Log.i(getClass().getName(), "Connection not created", e);
            }
        }

        public void doPost() {
            try {
                URL url = new URL("http://192.168.137.1:8080/beholder-master/post");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                byte[] data = new byte[100];
                while(true) {
                    Thread.currentThread().sleep(10000);
                    urlConnection.getOutputStream().write("Hi there".getBytes());
                    urlConnection.getOutputStream().flush();
                    int bytesRead = urlConnection.getInputStream().read(data);
                    Log.i(getClass().getName(), String.format("Post result is %s bytes received %d", new String(data), bytesRead));
                }
            } catch (Exception e) {
                Log.i(getClass().getName(), "Connection not created", e);
            }
        }

    }





}
