package com.android.cy.androidmazegame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.android.cy.androidmazegame.GamePad.GamePadMoveCallback;
import com.android.cy.androidmazegame.GamePad.GamePadView;

public class MainActivity extends Activity {

    private GameSurfaceView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameView = new GameSurfaceView(this);
        setContentView(mGameView);

//        EditText editText = new EditText(this);
//        editText.setText("Hello World");
//        addContentView(editText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Fake empty container layout
        RelativeLayout lContainerLayout = new RelativeLayout(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT ));

        // Custom view
     //   Button mCustomView = new Button(this);
     //   mCustomView.setText("Test");
        GamePadView gamePadView = new GamePadView(this);
        gamePadView.setMoveCallback(new GamePadMoveCallback() {
            @Override
            public void onMove(float x, float y) {
                mGameView.onMoveUpdate(x / 50, y / 50);
            }
        });

        RelativeLayout.LayoutParams lButtonParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT , RelativeLayout.LayoutParams.WRAP_CONTENT );
        lButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        gamePadView.setLayoutParams(lButtonParams);
        lContainerLayout.addView(gamePadView);

// Adding full screen container
        addContentView(lContainerLayout, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT ) );

 //       setContentView(R.layout.activity_main);
 //       mGameView = (GameSurfaceView)findViewById(R.id.gl_surfaece_view);
    }
}
