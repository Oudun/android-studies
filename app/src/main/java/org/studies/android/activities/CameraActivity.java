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
            // Camera is not available (in use or does not exist)
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

//   try {
//            urlConnection.setDoOutput(true);
//            urlConnection.setChunkedStreamingMode(0);
//
//            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//            writeStream(out);
//
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            readStream(in);
//        } finally {
//            urlConnection.disconnect();
//        }


        public Shooter() {
            camera = getCameraInstance();
            camera.startPreview();
            try {
                URL url = new URL("http://192.168.112.133:8080/beholder-master/upload");
                urlConnection = (HttpURLConnection)url.openConnection();
            } catch (Exception e) {
                Log.i(getClass().getName(), "Connection not created", e);
            }
        }

        @Override
        public void run() {
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
                                //try {
//                                    urlConnection.setDoOutput(true);
//                                    urlConnection.getOutputStream().write(data);
//                                    urlConnection.getOutputStream().flush();
                                    Log.i(getClass().getName(), String.format("Shot taken. size is %d bytes", data.length));
//                                } catch (IOException ee) {
//                                    Log.i(getClass().getName(), "Shot not sent", ee);
//                                }
                            }
                        });
                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), "Pause failed", e);
                }
            }
        }
    }


}
