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

    public String toString() {
        return "" + x + " " + y +" " + z;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public static void add(Vector3D result, Vector3D a, Vector3D b) {
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
    }

    public static void minus(Vector3D result, Vector3D a, Vector3D b) {
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
    }

    public static void normalize(Vector3D result, Vector3D t) {
        float l = t.length();
        result.x = t.x / l;
        result.y = t.y / l;
        result.z = t.z / l;
    }
}
