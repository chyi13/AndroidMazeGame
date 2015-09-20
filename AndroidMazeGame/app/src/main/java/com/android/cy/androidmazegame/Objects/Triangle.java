package com.android.cy.androidmazegame.Objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.android.cy.androidmazegame.GameRenderer;
import com.android.cy.androidmazegame.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2015/9/19.
 */
public class Triangle extends BasicObject{
    // This triangle is red, green, and blue.
    final float[] triangle1VerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f};

    // This triangle is yellow, cyan, and magenta.
    final float[] triangle2VerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            1.0f, 0.0f, 1.0f, 1.0f};

    // This triangle is white, gray, and black.
    final float[] triangle3VerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f};

    /** Store our model data in a float buffer. */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;

    /** How many elements per vertex. */
    private final int mStrideBytes = 7 * BYTES_PER_FLOAT;

    /** Offset of the position data. */
    private final int mPositionOffset = 0;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Offset of the color data. */
    private final int mColorOffset = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];

    public Triangle(Context context) {
        super(context);

        // Initialize the buffers.
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangle1Vertices.put(triangle1VerticesData).position(0);
        mTriangle2Vertices.put(triangle2VerticesData).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);

        // generate shader program
        generateShader();

    }

    @Override
    public void generateShader() {
        //
        int vertexShader = GameRenderer.loadShader(GLES20.GL_VERTEX_SHADER, RawResourceReader.readTextFileFromRawResource(contextHandle, R.raw.vertex_shader));
        int fragmentShader = GameRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, RawResourceReader.readTextFileFromRawResource(contextHandle, R.raw.fragment_shader));

        programHandle = GLES20.glCreateProgram();             // create empty OpenGL Program
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(programHandle, fragmentShader); // add the fragment shader to program

            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glBindAttribLocation(programHandle, 2, "a_Normal");
        }
        GLES20.glLinkProgram(programHandle);                  // create OpenGL program executables

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void draw(float[] mViewMatrix, float[] mProjectionMatrix, float[] mModelMatrix, float[] mLightPosInEyeSpace) {
        mTriangle1Vertices.position(mPositionOffset);

        // Pass in the position information
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, mTriangle1Vertices);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mTriangle1Vertices.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, mTriangle1Vertices);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
