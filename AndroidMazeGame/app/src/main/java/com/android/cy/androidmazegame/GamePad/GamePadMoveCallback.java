package com.android.cy.androidmazegame.GamePad;

/**
 * Created by Administrator on 2015/9/25.
 */
public interface GamePadMoveCallback {
    public void onMove(float x, float y);
    public void onKeyDown(int direction);
    public void onKeyUp();
}
