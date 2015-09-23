package com.android.cy.androidmazegame;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/9/19.
 */
public class GameSurfaceView extends GLSurfaceView {

    public final static float WINDOW_WIDTH = 2560;
    public final static float WINDOW_HEIGHT = 1600;

    private final GameRenderer mGameRenderer;

    public GameSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mGameRenderer = new GameRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mGameRenderer);
    }

    private float touchDownX, touchDownY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                mGameRenderer.updateCamera(e.getX() - touchDownX, e.getY() - touchDownY);
                touchDownX = e.getX();
                touchDownY = e.getY();
            }
            case MotionEvent.ACTION_DOWN: {
                touchDownX = e.getX();
                touchDownY = e.getY();
            }
        }
        return true;
    }
}
