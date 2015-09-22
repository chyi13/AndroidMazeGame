package com.android.cy.androidmazegame.Utils;

/**
 * Created by Administrator on 2015/9/22.
 */
public class Vector3D {
    public float x, y, z;

    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float[] toFloatArray() {
        return new float[]{x, y, z};
    }
}
