package ru.climbing.itmo.itmoclimbing.graphicPart.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.graphicPart.SimpleCamera;
import ru.climbing.itmo.itmoclimbing.graphicPart.utils.ShaderUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttrib3f;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Игорь on 19.10.2016.
 */

public class IntroToLightingRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private FloatBuffer vertexData;
    private int programId;
    private int pointProgramId;

    private SimpleCamera camera;

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] mLightModelMatrix = new float[16];
    /**
     * Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private final float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    /**
     * Used to hold the current position of the light in world space (after transformation via model matrix).
     */
    private final float[] mLightPosInWorldSpace = new float[4];

    /**
     * Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)
     */
    private final float[] mLightPosInEyeSpace = new float[4];
    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private float[] mMVPMatrix = new float[16];


    private float[] mMVMatrix = new float[16];


    private FloatBuffer mCubePositions;
    private FloatBuffer mCubeColors;
    private FloatBuffer mCubeNormals;
    private int uMVPMatrixLocation;
    private int uMVMatrixLocation;
    private int uLightPosLocation;
    private int aPositionLocation;
    private int aNormalLocation;
    private int aColorLocation;
    private int aPointPositionLocation;
    private int uPiontMVPMatrixLocation;

    public IntroToLightingRenderer(Context context) {
        this.context = context;

        bindData();
    }


    private void bindData() {
        final float[] cubePositionData =
                {
                        // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                        // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                        // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                        // usually represent the backside of an object and aren't visible anyways.

                        // Front face
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,

                        // Right face
                        1.0f, 1.0f, 1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        1.0f, -1.0f, -1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Back face
                        1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,
                        1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, -1.0f,

                        // Left face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, 1.0f, 1.0f,

                        // Top face
                        -1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, -1.0f,

                        // Bottom face
                        1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                        1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, 1.0f,
                        -1.0f, -1.0f, -1.0f,
                };

        // R, G, B, A
        final float[] cubeColorData =
                {
                        // Front face (red)
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f, 1.0f,

                        // Right face (green)
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,
                        0.0f, 1.0f, 0.0f, 1.0f,

                        // Back face (blue)
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,
                        0.0f, 0.0f, 1.0f, 1.0f,

                        // Left face (yellow)
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f,

                        // Top face (cyan)
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 1.0f,

                        // Bottom face (magenta)
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 0.0f, 1.0f, 1.0f
                };

        // X, Y, Z
        // The normal is used in light calculations and is a vector which points
        // orthogonal to the plane of the surface. For a cube model, the normals
        // should be orthogonal to the points of each face.
        final float[] cubeNormalData = {
                // Front face
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // Right face
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // Back face
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                // Left face
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                // Top face
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // Bottom face
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f
        };

        // Initialize the buffers.
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(cubeColorData).position(0);

        mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals.put(cubeNormalData).position(0);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //init camera
        float eyeX = 0f;
        float eyeY = 0f;
        float eyeZ = -0.5f;

        float lookAtX = 0f;
        float lookAtY = 0f;
        float lookAtZ = -5f;

        float upX = 0f;
        float upY = 1f;
        float upZ = 0f;

        camera = new SimpleCamera(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);

        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);

        int vertexShader = ShaderUtils.createShader(context,
                GL_VERTEX_SHADER, R.raw.vertex_intro_to_light_shader);
        int fragmentShader = ShaderUtils.createShader(context,
                GL_FRAGMENT_SHADER, R.raw.fragment_intro_to_light_shader);
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        //Linking
        uMVPMatrixLocation = glGetUniformLocation(programId, "u_MVPMatrix");
        uMVMatrixLocation = glGetUniformLocation(programId, "u_MVMatrix");
        uLightPosLocation = glGetUniformLocation(programId, "u_LightPos");
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        aNormalLocation = glGetAttribLocation(programId, "a_Normal");

        vertexShader = ShaderUtils.createShader(context,
                GL_VERTEX_SHADER, R.raw.vertex_point_shader);
        fragmentShader = ShaderUtils.createShader(context,
                GL_FRAGMENT_SHADER, R.raw.fragment_point_shader);

        pointProgramId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        aPointPositionLocation = glGetAttribLocation(pointProgramId, "a_Position");
        uPiontMVPMatrixLocation = glGetUniformLocation(pointProgramId, "u_MVPMatrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        Log.d("onSurfaceChanged", "width: " + width + " height: " + height);
        camera.updateProjectionMatrix(1f, 100f, width, height, 90);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float p;
        if (camera.height < camera.width) {
            p = (float) camera.height;
        } else {
            p = (float) camera.width;
        }

        float dX = accumulateX.getAndSet(0) / p;
        float dY = accumulateY.getAndSet(0) / p;

        camera.eyeX += dX;
        camera.eyeY += dY;

        camera.updateViewMatrix();
        camera.updateVPMatrix();

        long time = SystemClock.uptimeMillis() % 10000;
        float angle = (float) time / 10000f * 360f;


        //calculate position of light
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0f, 0f, -5f);
        Matrix.rotateM(mLightModelMatrix, 0, angle, 0f, 1f, 0f);
        Matrix.translateM(mLightModelMatrix, 0, 0f, 0f, 2f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, camera.mViewMatrix, 0, mLightPosInWorldSpace, 0);


        //draw cubes
        glUseProgram(programId);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 4f, 0f, -7f);
        Matrix.rotateM(mModelMatrix, 0, angle, 0f, 0f, 1f);
        drawCube();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
        drawCube();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angle, 0.0f, 0.0f, 1.0f);
        drawCube();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
        drawCube();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mModelMatrix, 0, angle, 1.0f, 1.0f, 0.0f);
        drawCube();

        //draw light
        glUseProgram(pointProgramId);
        drawLightPoint();


    }

    private void drawLightPoint() {
        glVertexAttrib3f(aPointPositionLocation, mLightPosInModelSpace[0],
                mLightPosInModelSpace[1], mLightPosInModelSpace[2]);
        glDisableVertexAttribArray(aPointPositionLocation);

        Matrix.multiplyMM(mMVPMatrix, 0, camera.mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, camera.mProjectionMatrix, 0, mMVPMatrix, 0);
        glUniformMatrix4fv(uPiontMVPMatrixLocation, 1, false, mMVPMatrix, 0);


        glDrawArrays(GL_POINTS, 0, 1);
    }

    private void drawCube() {
        //Pass in the position information
        mCubePositions.position(0);
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 0, mCubePositions);
        glEnableVertexAttribArray(aPositionLocation);

        //Pass in the color information
        mCubeColors.position(0);
        glVertexAttribPointer(aColorLocation, 4, GL_FLOAT, false, 0, mCubeColors);
        glEnableVertexAttribArray(aColorLocation);

        //Pass in the normals information
        mCubeNormals.position(0);
        glVertexAttribPointer(aNormalLocation, 3, GL_FLOAT, false, 0, mCubeNormals);
        glEnableVertexAttribArray(aNormalLocation);


        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVMatrix, 0, camera.mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the model view matrix.
        glUniformMatrix4fv(uMVMatrixLocation, 1, false, mMVMatrix, 0);

        // This multiplies the model view matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, camera.mProjectionMatrix, 0, mMVMatrix, 0);

        // Pass in the combined matrix.
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        glUniform3f(uLightPosLocation, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    private AtomicInteger accumulateX = new AtomicInteger(0);
    private AtomicInteger accumulateY = new AtomicInteger(0);

    public void rotateCam(float dx, float dy) {
        accumulateX.addAndGet(Math.round(dx));
        accumulateY.addAndGet(Math.round(dy));
    }

    public int getHeight() {
        return camera.height;
    }

    public int getWidth() {
        return camera.width;
    }
}
