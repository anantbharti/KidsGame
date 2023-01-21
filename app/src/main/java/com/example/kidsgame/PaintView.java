package com.example.kidsgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private static final int BACKGROUND = 0xFFDDDDDD;
    Map<TextView,TextView> viewMap = new HashMap<TextView,TextView>();
    private boolean isPathStarted = false;
    private Point start;
    private Point rawStart,rawEnd;

    public PaintView(Context context) {
        super(context);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public Map<TextView, TextView> getViewMap() {
        return viewMap;
    }

    public void setViewMap(Map<TextView, TextView> viewMap) {
        this.viewMap = viewMap;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rawStart = new Point((int)event.getRawX(),(int)event.getRawY());
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                rawEnd = new Point((int)event.getRawX(),(int)event.getRawY());
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        isPathStarted = true;
        start = new Point((int)x,(int)y);
    }

    private void touch_move(float x, float y) {
        // draw line with finger move
        if (isPathStarted) {
            mPath.reset();
            mPath.moveTo(start.x, start.y);
            mPath.lineTo(x, y);
        }
    }

    private void touch_up(float x, float y) {
        mPath.reset();
        if (isPathStarted) {

            Point end = new Point((int)x,(int)y);
            if(isCorrect())
                Toast.makeText(getContext(),"Correct",Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(getContext(),"Wrong",Toast.LENGTH_SHORT).show();
                return;
            }
            // move finished at valid point so draw whole line
            // start point
            mPath.moveTo(start.x, start.y);
            // end point
            mPath.lineTo(x, y);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
            isPathStarted = false;
        }

    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BACKGROUND);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    private boolean isCorrect(){
        for (Map.Entry<TextView,TextView> entry : viewMap.entrySet()) {
            TextView left = entry.getKey();
            TextView right = entry.getValue();

            if(isViewContains(left,rawStart.x,rawStart.y) && isViewContains(right,rawEnd.x,rawEnd.y))
                return true;
            if(isViewContains(right,rawStart.x,rawStart.y) && isViewContains(left,rawEnd.x,rawEnd.y))
                return true;
        }
        return false;
    }


    private boolean isViewContains(View view, int rx, int ry) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
    }
}