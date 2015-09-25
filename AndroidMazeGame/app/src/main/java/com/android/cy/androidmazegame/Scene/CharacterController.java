package com.android.cy.androidmazegame.Scene;

import android.opengl.Matrix;

import com.android.cy.androidmazegame.Utils.Vector3D;

/**
 * Created by Administrator on 2015/9/22.
 */
public class CharacterController {
    private Vector3D eyePos;
    private Vector3D targetPos;
    private Vector3D upDirection;

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private final float[] mViewMatrix = new float[16];

    public CharacterController() {
        eyePos = new Vector3D(-2.5f, 5.0f, -2.5f);
        targetPos = new Vector3D(-2.5f, 5.0f, -5.0f);
        upDirection = new Vector3D(0.0f, 1.0f, 0.0f);

        Matrix.setLookAtM(mViewMatrix, 0, eyePos.x, eyePos.y, eyePos.z,
                targetPos.x, targetPos.y, targetPos.z,
                upDirection.x, upDirection.y, upDirection.z);
    }

    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    public Vector3D getEyePos() { return eyePos; }
    public Vector3D getTargetPos() { return targetPos; }

    public void updateTarget(Vector3D t) {
        targetPos = t;
        updateViewMatrix();
    }

    public void updateEyePos(Vector3D e) {
        eyePos = e;
        updateViewMatrix();
    }

    private void updateViewMatrix() {
        Matrix.setLookAtM(mViewMatrix, 0, eyePos.x, eyePos.y, eyePos.z,
                targetPos.x + eyePos.x, targetPos.y + eyePos.y, targetPos.z + eyePos.z,
                upDirection.x, upDirection.y, upDirection.z);
    }
}
