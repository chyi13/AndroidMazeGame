package com.android.cy.androidmazegame;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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

    public GameSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mGameRenderer = new GameRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mGameRenderer);
    }


    public void onCameraTargetUpdate(float x, float y) {
        mGameRenderer.updateCamera(x, y);
    }

    public void onCharacterPosUpdate(int direction) {
        mGameRenderer.updateCharacterPos(direction);
    }
}
