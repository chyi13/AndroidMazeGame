package com.android.cy.androidmazegame.Scene;

import android.opengl.Matrix;
import android.util.Log;

import com.android.cy.androidmazegame.Utils.Vector3D;

/**
 * Created by Administrator on 2015/9/22.
 */
public class CharacterController {
    private Vector3D eyePos;
    private Vector3D targetPos;
    private Vector3D upDirection;

    private Vector3D moveDirection;

    private Vector3D moveDestination;

    /**
     *
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private final float[] mViewMatrix = new float[16];

    public CharacterController() {
        eyePos = new Vector3D(-2.5f, 2.0f, -2.5f);
        targetPos = new Vector3D(-2.5f, 2.0f, -5.0f);
        upDirection = new Vector3D(0.0f, 1.0f, 0.0f);

        moveDestination = new Vector3D(eyePos);
        moveDirection = new Vector3D();

        Matrix.setLookAtM(mViewMatrix, 0, eyePos.x, eyePos.y, eyePos.z,
                eyePos.x + targetPos.x, eyePos.y + targetPos.y, eyePos.z + targetPos.z,
                upDirection.x, upDirection.y, upDirection.z);

        // move direction vector
        moveDirection = new Vector3D();
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

    public void update(float delta) {
        // if moving
        if (!moveDirection.checkEmpty()) {
            Log.v("CharacterController", "update");
            Vector3D.add(eyePos, eyePos, Vector3D.multiplyVF(moveDirection, delta));
            updateViewMatrix();
        }
    }

    public void onKeyDown(int direction) {
        switch (direction) {
            // left
            case 0:
                moveDirection = new Vector3D(targetPos.z, 0.0f, -targetPos.x).normalize();
                break;
            // right
            case 1:
                moveDirection = new Vector3D(-targetPos.z, 0.0f, targetPos.x).normalize();
                break;
            // up
            case 2:
                moveDirection = new Vector3D(targetPos.x, 0.0f, targetPos.z).normalize();
                break;
            // down
            case 3:
                moveDirection = new Vector3D(-targetPos.x, 0.0f, -targetPos.z).normalize();
                break;
            default:
                break;
        }
        updateViewMatrix();
    }

    public void onKeyUp() {
        moveDirection.setXYZ(0, 0, 0);
    }

    private void updateViewMatrix() {
        Matrix.setLookAtM(mViewMatrix, 0, eyePos.x, eyePos.y, eyePos.z,
                targetPos.x + eyePos.x, targetPos.y + eyePos.y, targetPos.z + eyePos.z,
                upDirection.x, upDirection.y, upDirection.z);
    }
}
