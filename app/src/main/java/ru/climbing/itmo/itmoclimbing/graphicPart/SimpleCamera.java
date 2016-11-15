package ru.climbing.itmo.itmoclimbing.graphicPart;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by Игорь on 25.10.2016.
 */

public class SimpleCamera implements Camera {
    /**
     * You have to call updateCam after any change in public variables
     */
    public float eyeX = 1f;
    public float eyeY = 1f;
    public float eyeZ = 1;

    public float lookAtX = 0f;
    public float lookAtY = 0f;
    public float lookAtZ = 0f;

    public float upX = 0f;
    public float upY = 1f;
    public float upZ = 0f;

    public float near = 1;
    public float far = 10;
    /**
     * aspect = width / height
     */
    public float aspect = 1;
    public int height = 1;
    public int width = 1;

    public float mProjectionMatrix[] = new float[16];
    public float mViewMatrix[] = new float[16];
    public float mVPMatrix[] = new float[16];
    public float mInvertViewMatrix[] = new float[16];
    /**
     * fovy = 0 define ortho projection, else perspective
     */
    public float fovy = 90;


    public SimpleCamera(){}

    public SimpleCamera(float eyeX, float eyeY, float eyeZ,
                        float lookAtX, float lookAtY, float lookAtZ,
                        float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;

        this.lookAtX = lookAtX;
        this.lookAtY = lookAtY;
        this.lookAtZ = lookAtZ;

        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;

        updateCam();
    }

    public SimpleCamera(float eyeX, float eyeY, float eyeZ,
                           float lookAtX, float lookAtY, float lookAtZ,
                           float upX, float upY, float upZ,
                           float near, float far,
                           int width, int height, float fovy) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;

        this.lookAtX = lookAtX;
        this.lookAtY = lookAtY;
        this.lookAtZ = lookAtZ;

        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;

        this.width = width;
        this.height = height;

        this.near = near;
        this.far = far;
        this.aspect = (float)width / height;
        this.fovy = fovy;

        updateCam();
    }

    @Override
    public void getRay(@NonNull float[] dest, int offset, int screenX, int screenY) {
        if (dest.length - offset < 8) {
            throw new IllegalArgumentException("dest.length - offset < 8");
        }
        float viewRay[] = new float[8];
        viewRay[0] = viewRay[1] = viewRay[2] = 0;
        viewRay[3] = 1;

        float relativeX = (float) screenX / width;
        float relativeY= (float) screenY / height;
        float h = 2 * near * (float)Math.tan(fovy / 2);
        float w = aspect * h;
        viewRay[4] = - w / 2f + w * relativeX;
        viewRay[5] = - h / 2f + h * relativeY;
        viewRay[6] = near;
        viewRay[7] = 1;

        Matrix.multiplyMV(dest, 0, mInvertViewMatrix, 0, viewRay, 0);
        Matrix.multiplyMV(dest, 4, mInvertViewMatrix, 0, viewRay, 4);
    }

    @Override
    public void updateCam() {
        updateViewMatrix();
        updateProjectionMatrix();

        updateVPMatrix();
    }

    public void updateViewMatrix(float eyeX, float eyeY, float eyeZ,
                           float lookAtX, float lookAtY, float lookAtZ,
                           float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;

        this.lookAtX = lookAtX;
        this.lookAtY = lookAtY;
        this.lookAtZ = lookAtZ;

        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;

        updateViewMatrix();
    }

    public void updateViewMatrix() {
        //updateViewMatrix();
        Matrix.setLookAtM(mViewMatrix, 0,
                eyeX, eyeY, eyeZ,
                lookAtX, lookAtY, lookAtZ,
                upX, upY, upZ);
    }

    public void updateProjectionMatrix(float near, float far,
                                 int width, int height,
                                 float fovy) {

        this.width = width;
        this.height = height;

        this.near = near;
        this.far = far;
        this.aspect = (float)width / height;
        this.fovy = fovy;

        updateProjectionMatrix();
    }

    public void updateProjectionMatrix() {
        //updateProjectionMatrix();
        if (fovy == 0f) {
            float left = -1;
            float right = 1;
            float bottom = -1;
            float top = 1;
            //if landscape
            if (aspect > 1) {
                left *= aspect;
                right *= aspect;
            } else {
                top *= aspect;
                bottom *= aspect;
            }
            Matrix.orthoM(mProjectionMatrix, 0,
                    left, right, bottom, top,
                    near, far);
        } else {
            Matrix.perspectiveM(mProjectionMatrix, 0, fovy, aspect, near, far);
        }
    }

    public void updateVPMatrix() {
        //updateVPMatrix();
        Matrix.multiplyMM(mVPMatrix, 0, mViewMatrix, 0, mProjectionMatrix, 0);

        //update invert view matrix
        Matrix.invertM(mInvertViewMatrix, 0, mViewMatrix, 0);
    }
}
