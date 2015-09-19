package com.android.cy.androidmazegame;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GameSurfaceView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameView = new GameSurfaceView(this);
        setContentView(mGameView);
    }
}
