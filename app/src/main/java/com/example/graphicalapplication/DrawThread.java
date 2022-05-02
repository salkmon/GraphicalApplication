package com.example.graphicalapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true; // Остановка потока
    private Paint backgroundPaint = new Paint();
    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }
    private Bitmap bucket;
    private int bucketX = 5;
    private int towardPointX;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        bucket = BitmapFactory.decodeResource(context.getResources(), R.drawable.bucket);
        this.surfaceHolder = surfaceHolder;
    }

    public void requestStop() {
        running = false;
    }

    public void moveTo(int x) {
        towardPointX = x;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(bucket, bucketX, (int) (canvas.getWidth() * 0.9), backgroundPaint);

                    if(bucketX + bucket.getWidth() / 2 < towardPointX) bucketX += 10;
                    if(bucketX + bucket.getWidth() / 2 > towardPointX) bucketX -= 10;


                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}