package org.studies.android.activities;

import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
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

import static java.security.AccessController.getContext;

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
        Log.i(CameraActivity.class.getName(), "Getting Camera");
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e(Camera.class.getName(), "Failed to create a camera", e);
        }
        return c; // returns null if camera is unavailable

//        android.hardware.camera2.CameraManager cameraManager = new CameraManager(getContext());

    }

    class ShotGrabber implements Camera.PictureCallback {
        byte[] data;
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                Log.d(getClass().getName(), String.format("Getting %d bytes of data from camera",
                        data.length));
                this.data = data;
            } else {
                Log.d(getClass().getName(), "Data is null");
            }
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

    class ShooterPreview implements Camera.PreviewCallback {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data != null)
                Log.i(getClass().getName(), String.format("Camera preview size %d", data.length));
            else
                Log.i(getClass().getName(), "No preview data");
        }
    }

    class Shooter extends Thread {

        int delay = 5000;
        Camera camera;
        HttpURLConnection urlConnection;
        ShotGrabber jpegCallback, rawCallback;
        ShooterError shooterError;
        ShooterPreview shooterPreview = new ShooterPreview();

        public Shooter() {
            jpegCallback = new ShotGrabber();
            rawCallback = new ShotGrabber();
            camera = getCameraInstance();
            shooterError = new ShooterError();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPictureSize(1280, 960);
            camera.setParameters(parameters);
            camera.setErrorCallback(shooterError);
            camera.setPreviewCallback(shooterPreview);
        }

        @Override
        public void run() {
            while(true) {
                try {
                    camera.startPreview();
                    Log.i(getClass().getName(), "Taking picture");
                    camera.takePicture(null, rawCallback, jpegCallback);
                    if (jpegCallback.getData() != null) {
                        doPost(jpegCallback.getData());
                    } else {
                        Log.i(getClass().getName(), "JPEG not captured");
                    }
                    if (rawCallback.getData() != null) {
                        doPost(rawCallback.getData());
                    } else {
                        Log.i(getClass().getName(), "RAW not captured");
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
                //URL url = new URL("http://192.168.137.1:8080/beholder-master/post");
                Log.i(getClass().getName(), String.format("Posting %d bytes", data.length));
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
