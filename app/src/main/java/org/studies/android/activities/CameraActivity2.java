package org.studies.android.activities;

import android.content.Context;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import org.studies.android.R;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.ImageFormat.JPEG;
import static android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE;

public class CameraActivity2 extends AppCompatActivity {

    CaptureRequest.Builder captureRequestBuilder;

    CameraDevice camera;

    ImageReader imageReader;

    Handler shooterHandler = null;

    ShooterThread shooterThread;

    int delay = 10000;

    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String[] cameraList = cameraManager.getCameraIdList();
            Log.d("CAMERA_2", cameraList.toString());
            cameraManager.openCamera(cameraList[0], deviceStateCallback, shooterHandler);
        } catch (SecurityException e) {
            Log.e("CAMERA_2", e.getMessage());
        } catch (Exception e) {
            Log.e("CAMERA_2", e.getMessage());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        shooterThread = new ShooterThread();
        shooterThread.start();
    }

    CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
            Log.d("CAMERA_2", "onCaptureStarted");
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            Log.d("CAMERA_2", "onCaptureProgressed");
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Log.d("CAMERA_2", "onCaptureCompleted result is " + result.toString());
            Image image = null;
            try {
                image = imageReader.acquireNextImage();
                byte[] buffer = new byte[image.getPlanes()[0].getBuffer().remaining()];
                image.getPlanes()[0].getBuffer().get(buffer);
                //Log.d("CAMERA_2", "Buffer content is " + new String(buffer));
                Log.d("CAMERA_2", "Buffer content size is " + buffer.length);
                new PostTask(buffer).execute();
                try {
                    CaptureRequest captureRequest = captureRequestBuilder.build();
                    session.capture(captureRequest, captureCallback, shooterHandler);
                    Thread.currentThread().sleep(delay);
                } catch (Exception ee){
                    Log.e("CAMERA_2", "Fail to sleep", ee);
                }
            } catch (Exception e) {
                Log.e("CAMERA_2", e.getLocalizedMessage());
            } finally {
                if (image != null)
                    image.close();
            }
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Log.d("CAMERA_2", "onCaptureFailed");
        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
            Log.d("CAMERA_3", "onCaptureSequenceCompleted");
        }

        @Override
        public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
            super.onCaptureSequenceAborted(session, sequenceId);
            Log.d("CAMERA_2", "onCaptureSequenceAborted");
        }

        @Override
        public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
            super.onCaptureBufferLost(session, request, target, frameNumber);
            Log.d("CAMERA_2", "onCaptureBufferLost");
        }
    };

    // Capture session state callback
    CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.d("CAMERA_2", "onConfigured");
            captureRequestBuilder.addTarget(imageReader.getSurface());
            CaptureRequest captureRequest = captureRequestBuilder.build();
            try {
                session.capture(captureRequest, captureCallback, shooterHandler);
            } catch (Exception e) {
                Log.e("CAMERA_2", "Fail to capture", e);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.d("CAMERA_2", "onConfigureFailed");
        }

    };

    // Device state callback
    CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            camera = cameraDevice;
            Log.d("CAMERA_2", "Camera is opened");
            imageReader = ImageReader.newInstance(1920, 1088, JPEG, 1);
            List<Surface> outputs = new ArrayList<Surface>();
            outputs.add(imageReader.getSurface());
            try {
                captureRequestBuilder = camera.createCaptureRequest(TEMPLATE_STILL_CAPTURE);
                camera.createCaptureSession(outputs, sessionStateCallback, shooterHandler);
            } catch (Exception e) {
                Log.e("CAMERA_2", "Camera error ", e);
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d("CAMERA_2", "Camera is disconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.d("CAMERA_2", "Camera error " + error);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null)
            camera.close();
        Log.i(null, "Camera is closed");
    }

    public void doPost(byte[] data) {
        try {
            Log.i("CAMERA_2", String.format("Posting %d bytes", data.length));
            //URL url = new URL("http://192.168.137.1:8080/beholder-master/post");
            URL url = new URL("http://192.168.0.194:8080/beholder-master/post");
            //URL url = new URL("http://django-psql-persistent-toolbox.7e14.starter-us-west-2.openshiftapps.com");
            //URL url = new URL("http://ya.ru/");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.getContent();
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            byte[] buffer = new byte[100];
            while(is.read(buffer) > -1) {
                urlConnection.getOutputStream().write(buffer);
                urlConnection.getOutputStream().flush();
            }
            byte[] responseData = new byte[128];
            DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
            Byte responseByte = dis.readByte();
            Log.i("CAMERA_2", String.format("Post result is %s", Integer.toBinaryString(responseByte)));
            setDelay(responseByte);
        } catch (Exception e) {
            Log.i("CAMERA_2", "Connection not created");
        }
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

    class PostTask extends AsyncTask {

        byte[] data;

        public PostTask(byte[] data) {
            this.data = data;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            doPost(data);
            return null;
        }

    }

    class ShooterThread extends Thread {

        public void run() {
            Looper.prepare();

            shooterHandler = new Handler() {
                public void handleMessage(Message msg) {
                    // process incoming messages here
                }
            };

            Looper.loop();
        }
    }

}


