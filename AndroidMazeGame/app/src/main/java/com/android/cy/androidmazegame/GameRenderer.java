package com.android.cy.androidmazegame;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.android.cy.androidmazegame.Objects.BasicObject;
import com.android.cy.androidmazegame.Objects.Cube;
import com.android.cy.androidmazegame.Objects.Plane;
import com.android.cy.androidmazegame.Objects.Wall;
import com.android.cy.androidmazegame.SceneManager.SceneManager;
import com.android.cy.androidmazegame.Utils.Vector3D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2015/9/19.
 */
public class GameRenderer implements GLSurfaceView.Renderer{

    private BasicObject object;
    private BasicObject plane;
    private BasicObject wall;


    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private final float[] mProjectionMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private final float[] mViewMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] mLightModelMatrix = new float[16];

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    private final float[] mLightPosInWorldSpace = new float[4];

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    private final float[] mLightPosInEyeSpace = new float[4];

    /** Context */
    private final Context mContextHandle;

    /** SceneManger */
    private SceneManager sceneManager;

    public GameRenderer(Context context) { mContextHandle = context;}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Culling back
        GLES20.glCullFace(GLES20.GL_BACK);

        // Front face
        GLES20.glFrontFace(GLES20.GL_CCW);

        // Position the eye behind the origin.
        final float eyeX = 20.0f;
        final float eyeY = 10.0f;
        final float eyeZ = 50.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 5.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        //
//        object = new Triangle(mContextHandle);
        object = new Cube(mContextHandle);
        plane = new Plane(mContextHandle, 500.f, 500.f);
        wall = new Wall(mContextHandle, 10.f, 20.f);

        sceneManager = new SceneManager(mContextHandle);
        sceneManager.setViewMatrix(mViewMatrix);
        sceneManager.readMazeMap();
    //    sceneManager.addObject(wall);
        sceneManager.addObject(plane);
    //    wall.setPosition(new Vector3D(0.0f, 0.0f, -10.f));
        plane.setPosition(new Vector3D(0.0f, -8.0f, 0.0f));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

        // Set Projection matrix for scenemanager
        sceneManager.setProjectionMatrix(mProjectionMatrix);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        //     Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.f, 1.f, 0.f);
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        sceneManager.setLightPosInEyeSpace(mLightPosInEyeSpace);
        sceneManager.render();
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
