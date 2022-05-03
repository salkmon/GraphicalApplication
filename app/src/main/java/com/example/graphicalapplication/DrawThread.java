package com.example.graphicalapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Rain {
    int x;
    int y;

    Rain(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
public class DrawThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true; // Остановка потока
    private Paint backgroundPaint = new Paint();
    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }
    private long lastDropTime;
    private Bitmap rainDrop;
    ArrayList<Rain> rainDrops = new ArrayList<>();

    private Bitmap bucket;

    private int bucketX = 5;
    private int towardPointX;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        bucket = BitmapFactory.decodeResource(context.getResources(), R.drawable.bucket);
        rainDrop = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop);
        this.surfaceHolder = surfaceHolder;
    }

    public void requestStop() {
        running = false;
    }

    public void moveTo(int x) {
        towardPointX = x;
    }

    public void spawnRain(int width, int height) {
        Random rand = new Random();
        rainDrops.add(new Rain(
                Math.abs(rand.nextInt() % width), (int)(height * 0.1)
        ));
        lastDropTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    int width = canvas.getWidth();
                    int height = canvas.getHeight();
                    canvas.drawRect(0, 0, width, height, backgroundPaint);
                    canvas.drawBitmap(bucket, bucketX, (int) (height * 0.8), backgroundPaint);

                    if(bucketX + bucket.getWidth() / 2 < towardPointX) bucketX += 10;
                    if(bucketX + bucket.getWidth() / 2 > towardPointX) bucketX -= 10;

                    if(System.currentTimeMillis() - lastDropTime > 3000)
                        spawnRain(width, height);

                    if(rainDrops != null) {
                        for(int i = 0; i < rainDrops.size(); i++) {
                            Rain element = rainDrops.get(i);
                            if(Rect.intersects(
                                    new Rect(bucketX, (int) (height * 0.8),
                                            bucketX + bucket.getWidth(), (int) (height * 0.8) * bucket.getHeight()),
                                    new Rect(element.x, element.y,
                                            element.x + rainDrop.getWidth(), element.y + rainDrop.getHeight())
                            ) || element.y > height) {
                                rainDrops.remove(element);
                                i--;
                            } else {
                                canvas.drawBitmap(rainDrop, element.x, element.y, backgroundPaint);
                                element.y += 10;
                            }
                        }
                    }

                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}