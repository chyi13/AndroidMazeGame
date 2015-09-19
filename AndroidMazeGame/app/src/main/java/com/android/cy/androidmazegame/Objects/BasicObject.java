package com.android.cy.androidmazegame.Objects;

import android.content.Context;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Administrator on 2015/9/19.
 */
public abstract class BasicObject {
    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer colorBuffer;

    public int programHandle;

    public Context contextHandle;

    /** How many bytes per float. */
    public final int mBytesPerFloat = 4;

    /** This will be used to pass in the transformation matrix. */
    public int mMVPMatrixHandle;

    /** This will be used to pass in the modelview matrix. */
    public int mMVMatrixHandle;

    /** This will be used to pass in the light position. */
    public int mLightPosHandle;

    /** This will be used to pass in model position information. */
    public int mPositionHandle;

    /** This will be used to pass in model color information. */
    public int mColorHandle;

    /** This will be used to pass in model normal information. */
    public int mNormalHandle;

    public BasicObject(Context context) {
        contextHandle = context;
    }

    // shader
    public abstract void generateShader();

    // draw
    public abstract void draw(float[] mViewMatrix, float[] mProjectionMatrix, float[] mModelMatrix);
}
