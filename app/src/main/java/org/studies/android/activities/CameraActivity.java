package org.studies.android.activities;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.studies.android.R;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Policy;

public class CameraActivity extends AppCompatActivity {

    Shooter shooter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        shooter = new Shooter();
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

        byte[] data;

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }
    }

    class ShooterError implements Camera.ErrorCallback {
        @Override
        public void onError(int error, Camera camera) {
            Log.i(getClass().getName(), String.format("Camera error %d", error));
        }
    }

    class Shooter extends Thread {

        int delay = 5000;

        Camera camera;

        HttpURLConnection urlConnection;

        ShotGrabber callback;

        ShooterError shooterError;

        public Shooter() {
            callback = new ShotGrabber();
            camera = getCameraInstance();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureSize(1280, 960);
            camera.setParameters(parameters);
            camera.setErrorCallback(shooterError);
        }

        @Override
        public void run() {
            while(true) {
                try {
                    camera.startPreview();
                    camera.takePicture(null, null, callback);
                    if (callback.getData() != null) {
                        doPost(callback.getData());
                    }
                    camera.stopPreview();
                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                        Log.e(getClass().getName(), "Thread interrupted");
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), String.format("Runtime exception %s", e.getMessage()));
                }
            }
        }

        public void doPost(byte[] data) {
            try {
                //URL url = new URL("http://192.168.0.194:8080/beholder-master/post");
                URL url = new URL("http://192.168.0.194:8080/beholder-master/post");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                ByteArrayInputStream is = new ByteArrayInputStream(data);
                byte[] buffer = new byte[100];
                while(is.read(buffer) > -1) {
                    urlConnection.getOutputStream().write(buffer);
                    urlConnection.getOutputStream().flush();
                }
                byte[] responseData = new byte[128];
               DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
               Byte responseByte = dis.readByte();
               Log.i(getClass().getName(), String.format("Post result is %s", Integer.toBinaryString(responseByte)));
               setDelay(responseByte);
            } catch (Exception e) {
                Log.i(getClass().getName(), "Connection not created", e);
            }
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            camera.stopPreview();
            camera.release();
        }

        public void setDelay(Byte delayByte) {

            int delayState = delayByte & 56;

            switch (delayState) {
                case 0: { delay = 0 ; break; }
                case 8: { delay = 1000 ; break; }
                case 16: { delay = 5000 ; break; }
                case 24: { delay = 10000 ; break; }
                case 32: { delay = 30000 ; break; }
                case 48: { delay = 60000 ; break; }
                case 56: { delay = 150000 ; break; }
                default: { delay = 5000;}
            }
        }

    }

}
