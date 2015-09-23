package com.android.cy.androidmazegame;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.android.cy.androidmazegame.Objects.BasicObject;
import com.android.cy.androidmazegame.Objects.Cube;
import com.android.cy.androidmazegame.Objects.Plane;
import com.android.cy.androidmazegame.Scene.SceneManager;
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
    private BasicObject roof;

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

    // Position the eye behind the origin.
    private float eyeX = -2.5f;
    private float eyeY = 5.0f;
    private float eyeZ = -2.5f;

    // We are looking toward the distance
    private float lookX = -2.5f;
    private float lookY = 5.0f;
    private float lookZ = -5.0f;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    final float upX = 0.0f;
    final float upY = 1.0f;
    final float upZ = 0.0f;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Use culling to remove back faces.
   //     GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Culling back
        GLES20.glCullFace(GLES20.GL_BACK);

        // Front face
        GLES20.glFrontFace(GLES20.GL_CCW);

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        //
//        object = new Triangle(mContextHandle);
        object = new Cube(mContextHandle);
        plane = new Plane(mContextHandle, 110.f, 110.f);
        roof = new Plane(mContextHandle, 110.f, 110.f);

        sceneManager = new SceneManager(mContextHandle);
        sceneManager.setViewMatrix(mViewMatrix);
        sceneManager.readMazeMap();
        sceneManager.addObject(plane);
        plane.setPosition(new Vector3D(0.0f, -10.0f, 0.0f));
        sceneManager.addObject(roof);
        roof.setPosition(new Vector3D(0.0f, 10.0f, 0.0f));
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

        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        sceneManager.setLightPosInEyeSpace(mLightPosInEyeSpace);
        sceneManager.render();
    }

    private float theta = 0, phi = 0;

    public void updateCamera(float x, float y) {
        theta += x / 300;
        phi += y / 300;

        Vector3D target = new Vector3D();
        target.x = (float) (Math.cos(theta) * Math.sin(phi));
        target.y = (float) Math.cos(-phi);
        target.z = (float) (Math.sin(theta) * Math.sin(phi));
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ,
                                            eyeX + target.x, eyeY + target.y, eyeZ + target.z,
                                            0, 1, 0);
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
