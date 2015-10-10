package com.android.cy.androidmazegame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.android.cy.androidmazegame.GamePad.GamePadMoveCallback;
import com.android.cy.androidmazegame.GamePad.GamePadView;
import com.android.cy.androidmazegame.GameView.GameSurfaceView;
import com.android.cy.androidmazegame.GameView.GameViewCallback;

public class MainActivity extends Activity {

    private GameSurfaceView mGameView;
    private GamePadView mGamePadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Game view
        mGameView = new GameSurfaceView(this);
        mGameView.setGameViewCallback(new GameViewCallback() {
            @Override
            public void onGameStart() {
                mGamePadView.startTimer();
            }
        });
        setContentView(mGameView);

        // Fake empty container layout
        RelativeLayout lContainerLayout = new RelativeLayout(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT ));

        // Custom view
        mGamePadView = new GamePadView(this);
        mGamePadView.setMoveCallback(new GamePadMoveCallback() {
            @Override
            public void onMove(float x, float y) {
                mGameView.onCameraTargetUpdate(x, y);
            }

            @Override
            public void onKeyDown(int direction) {
                mGameView.onCharacterKeyDown(direction);
            }

            @Override
            public void onKeyUp() { mGameView.onCharacterKeyUp(); }
        });

        RelativeLayout.LayoutParams lButtonParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT , RelativeLayout.LayoutParams.WRAP_CONTENT );
        lButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mGamePadView.setLayoutParams(lButtonParams);
        lContainerLayout.addView(mGamePadView);

// Adding full screen container
        addContentView(lContainerLayout, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT ) );

 //       setContentView(R.layout.activity_main);
 //       mGameView = (GameSurfaceView)findViewById(R.id.gl_surfaece_view);
    }
}
