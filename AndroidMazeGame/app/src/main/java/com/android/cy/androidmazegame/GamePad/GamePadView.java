package com.android.cy.androidmazegame.GamePad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.cy.androidmazegame.Utils.Vector2D;

/**
 * Created by Administrator on 2015/9/25.
 */
public class GamePadView extends View {

    private boolean isMoving = false;

    private Paint centerPaint, keyUpPaint, keyDownPaint, bgPaint;
    private Vector2D centerPos, leftPos, rightPos, upPos, downPos;
    private float centerRadius;
    private float hWidth, hHeight;
    private boolean[] keyStatus = { false, false, false, false};

    private int pointerID;

    private GamePadMoveCallback moveCallback;

    public GamePadView(Context context) {
        super(context);

        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.DKGRAY);
        centerPaint.setStrokeWidth(1);
        centerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        keyUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        keyUpPaint.setColor(Color.LTGRAY);
        keyUpPaint.setAlpha(160);
        keyUpPaint.setStrokeWidth(1);
        keyUpPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        keyDownPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        keyDownPaint.setColor(Color.DKGRAY);
        keyUpPaint.setAlpha(64);
        keyDownPaint.setStrokeWidth(1);
        keyDownPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.DKGRAY);
        bgPaint.setStrokeWidth(1);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public GamePadView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(centerPos.x, centerPos.y, centerRadius, centerPaint);

        canvas.drawRect(leftPos.x - hWidth, leftPos.y - hHeight, leftPos.x + hWidth, leftPos.y + hHeight, keyStatus[0] ? keyDownPaint : keyUpPaint);
        canvas.drawRect(rightPos.x - hWidth, rightPos.y - hHeight, rightPos.x + hWidth , rightPos.y + hHeight, keyStatus[1] ? keyDownPaint : keyUpPaint);
        canvas.drawRect(upPos.x - hWidth, upPos.y - hHeight, upPos.x + hWidth , upPos.y + hHeight, keyStatus[2] ? keyDownPaint : keyUpPaint);
        canvas.drawRect(downPos.x - hWidth, downPos.y - hHeight, downPos.x + hWidth, downPos.y + hHeight, keyStatus[3] ? keyDownPaint : keyUpPaint);

//        canvas.drawCircle(centerPos.x, centerPos.y, bgRadius, bgPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Here we make sure that we have a perfect circle
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measuredHeight);

        centerPos = new Vector2D(measuredWidth * 0.18f, measuredHeight - measuredHeight * 0.2f);

        Log.v("GamePadView", measuredWidth + " " + measuredHeight);
        centerRadius = measuredWidth * 0.01f;
        hWidth = measuredWidth * 0.03f;
        hHeight = measuredWidth * 0.03f;
        leftPos = new Vector2D(centerPos.x - 6.5f * centerRadius, centerPos.y);
        rightPos = new Vector2D(centerPos.x + 6.5f * centerRadius, centerPos.y);
        upPos = new Vector2D(centerPos.x, centerPos.y + 6.5f * centerRadius);
        downPos = new Vector2D(centerPos.x, centerPos.y - 6.5f * centerRadius);

        Log.v("GamePadView", (leftPos.x - hWidth) + " " + leftPos.y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // get pointer index from the event object
        int pointerIndex = e.getActionIndex();

        // get pointer ID
        int pointerID = e.getPointerId(pointerIndex);

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (isMoving)
                    processMotion(e.getX(), e.getY());
                return false;
            }
            case MotionEvent.ACTION_DOWN: {
                processKeyDown(e.getX(), e.getY());
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return false;
        }
        return false;
    }

    private void processMotion(float x, float y) {

     //  invalidate();
    }

    private boolean processKeyDown(float x, float y) {
        Vector2D tPos = new Vector2D(x, y);
        int key = -1;
        if (Vector2D.isInRect(tPos, leftPos, hWidth, hHeight)) {
            key = 0;
        }
        if (Vector2D.isInRect(tPos, rightPos, hWidth, hHeight)) {
            key = 1;
        }
        if (Vector2D.isInRect(tPos, upPos, hWidth, hHeight)) {
            key = 2;
        }
        if (Vector2D.isInRect(tPos, downPos, hWidth, hHeight)) {
            key = 3;
        }

        for (int i = 0; i< keyStatus.length; i++) {
            if (i == key) {
                keyStatus[i] = true;
            } else {
                keyStatus[i] = false;
            }
        }

        invalidate();

        if (key != -1) {
            moveCharacter(key);
            return true;
        } else {
            return false;
        }
    }

    private void moveCharacter(int direction) {
        moveCallback.onKeyDown(direction);
    }

    public void setMoveCallback(GamePadMoveCallback callback) {
        moveCallback = callback;
    }
}
