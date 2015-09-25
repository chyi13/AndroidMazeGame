package com.android.cy.androidmazegame.GamePad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.cy.androidmazegame.Utils.Vector2D;

/**
 * Created by Administrator on 2015/9/25.
 */
public class GamePadView extends View {

    private ShapeDrawable shapeDrawable;

    private Vector2D center;
    private Vector2D bgCenter;
    private Vector2D handleCenter;
    private float bgRadius;
    private boolean isMoving = false;

    private Paint bgPaint, handlePaint;

    private GamePadMoveCallback moveCallback;

    public GamePadView(Context context) {
        super(context);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.DKGRAY);
        bgPaint.setStrokeWidth(1);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handlePaint.setColor(Color.LTGRAY);
        handlePaint.setStrokeWidth(1);
        handlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public GamePadView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void onDraw(Canvas canvas) {
        canvas.save();

        canvas.drawCircle(bgCenter.x, bgCenter.y, bgRadius, bgPaint);

        canvas.drawCircle(handleCenter.x, handleCenter.y, bgRadius / 2, handlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Here we make sure that we have a perfect circle
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measuredHeight);

        center = new Vector2D(measuredWidth/2, measuredHeight/2);
        bgCenter = new Vector2D(measuredWidth * 0.18f, measuredHeight - measuredHeight * 0.2f);
        handleCenter = new Vector2D(bgCenter);
        bgRadius = measuredWidth * 0.1f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (isMoving)
                    processMotion(e.getX(), e.getY());
                return true;
            }
            case MotionEvent.ACTION_DOWN: {
                Log.v("GamePadView", "Down");
                float offsetX = e.getX() - bgCenter.x, offsetY = e.getY() - bgCenter.y;

                if (Math.abs(offsetX) < bgRadius && Math.abs(offsetY) < bgRadius) {
                    isMoving = true;
                } else
                    isMoving = false;
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.v("GamePadView", "Up");
                handleCenter.x = bgCenter.x;
                handleCenter.y = bgCenter.y;
                isMoving = false;
                invalidate();
                break;
        }
        return false;
    }

    private void processMotion(float x, float y) {
        float offsetX = x - bgCenter.x, offsetY = y - bgCenter.y;
        Log.v("GamePadView", offsetX + " " + offsetY);
        float distance = (float)Math.sqrt(offsetX * offsetX + offsetY * offsetY);

        if (distance< bgRadius * 0.9f) {
            handleCenter.x = x;
            handleCenter.y = y;
        } else {
            float ratio = bgRadius / distance;
            handleCenter.x = bgCenter.x +  ratio * offsetX;
            handleCenter.y = bgCenter.y + ratio * offsetY;
        }

        moveCallback.onMove(offsetX, offsetY);

        invalidate();
    }

    public void setMoveCallback(GamePadMoveCallback callback) {
        moveCallback = callback;
    }
}
