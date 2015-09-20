package com.android.cy.androidmazegame.Objects;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Administrator on 2015/9/19.
 */
public abstract class BasicObject {
    public float[] positionData;
    public float[] colorData;
    public float[] normalData;
    public short[] indexData;
    public float[] textureData;


    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer colorBuffer;
    public FloatBuffer textureBuffer;

    public int programHandle;

    public Context contextHandle;

    /** How many bytes per float. */
    public static final int BYTES_PER_FLOAT = 4;

    public static final int BYTES_PER_SHORT = 2;

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

    /** This will be used to pass in model texture coordinate information. */
    public int mTextureCoordinateHandle;

    /** This will be used to pass in the texture. */
    public int mTextureUniformHandle;

    /** This is a handle to our texture data. */
    public int mTextureDataHandle;

    /** Size of the position data in elements. */
    public final int mPositionDataSize = 3;

    /** Size of the color data in elements. */
    public final int mColorDataSize = 4;

    /** Size of the normal data in elements. */
    public final int mNormalDataSize = 3;

    /** Size of the texture coordinate data in elements. */
    public final int mTextureCoordinateDataSize = 2;

    public BasicObject(Context context) {
        contextHandle = context;
    }

    // shader
    public abstract void generateShader();

    // draw
    public abstract void draw(float[] mViewMatrix, float[] mProjectionMatrix, float[] mModelMatrix, float[] mLightPosInEyeSpace);

    public void initializeBuffers() {
        // Initialize the buffers.
        vertexBuffer = ByteBuffer.allocateDirect(positionData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(positionData).position(0);

        colorBuffer = ByteBuffer.allocateDirect(colorData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(colorData).position(0);

        normalBuffer = ByteBuffer.allocateDirect(normalData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        normalBuffer.put(normalData).position(0);

        if (indexData.length != 0) {
            indexBuffer = ByteBuffer.allocateDirect(indexData.length * BYTES_PER_SHORT)
                    .order(ByteOrder.nativeOrder()).asShortBuffer();
            indexBuffer.put(indexData).position(0);
        }

        textureBuffer = ByteBuffer.allocateDirect(textureData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(textureData).position(0);
    }
}
