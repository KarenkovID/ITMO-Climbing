package ru.climbing.itmo.itmoclimbing.graphicPart;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import ru.climbing.itmo.itmoclimbing.graphicPart.renderers.IntroToLightingRenderer;


/**
 * Created by Игорь on 17.10.2016.
 */

public class GLActivity extends Activity implements View.OnTouchListener{

    private GLSurfaceView glSurfaceView;

    public static final String TAG = "OnTouchEvent";

    IntroToLightingRenderer renderer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        Log.d("CASE LOG", "" + getIntent().getIntExtra("Renderer", -1));
        renderer = new IntroToLightingRenderer(this);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setOnTouchListener(this);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }

    float prevX, prevY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN x: " + event.getX() + "; y: " + event.getY() + " width: " + glSurfaceView.getWidth() + " height: " + glSurfaceView.getHeight());
                prevX = event.getX();
                prevY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE x: " + event.getX() + "; y: " + event.getY());
                float dx = event.getX() - prevX;
                float dy = event.getY() - prevY;
//                try {
//                    Thread.currentThread().sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                renderer.rotateCam(dx, -dy);
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP x: " + event.getX() + "; y: " + event.getY());
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
