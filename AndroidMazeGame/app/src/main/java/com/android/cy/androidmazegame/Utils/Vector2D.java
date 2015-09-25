package com.android.cy.androidmazegame.Utils;

/**
 * Created by Administrator on 2015/9/22.
 */
public class Vector2D {

    public float x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public float[] toFloatArray() {
        return new float[]{x, y};
    }
}
