package com.android.cy.androidmazegame.Scene;

import com.android.cy.androidmazegame.Utils.Vector3D;

/**
 * Created by Administrator on 2015/9/22.
 */
public class CharacterController {
    private Vector3D eyePos;
    private Vector3D targetPos;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    final float upX = 0.0f;
    final float upY = 1.0f;
    final float upZ = 0.0f;

    public CharacterController() {
        eyePos = new Vector3D(20.0f, 10.0f, 50.0f);
        targetPos = new Vector3D(0.0f, 5.0f, -5.0f);
    }

    public Vector3D getEyePos() { return eyePos; }
    public Vector3D getTargetPos() { return targetPos; }

    public void updateTarget(Vector3D newTarget) {
        targetPos = newTarget;
    }
}
