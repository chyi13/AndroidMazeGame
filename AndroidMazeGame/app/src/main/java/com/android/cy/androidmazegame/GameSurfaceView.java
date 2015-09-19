package com.android.cy.androidmazegame;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Administrator on 2015/9/19.
 */
public class GameSurfaceView extends GLSurfaceView {

    private final GameRenderer mGameRenderer;

    public GameSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mGameRenderer = new GameRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mGameRenderer);
    }
}
