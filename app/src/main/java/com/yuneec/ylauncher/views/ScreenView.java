package com.yuneec.ylauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yuneec.ylauncher.R;

import java.util.Random;

public class ScreenView extends FrameLayout {
    public ScreenView(Context context) {
        this(context, null);
    }

    public ScreenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int wallpaper = R.drawable.wallpaper_1;
    private ImageView imageView;
    private Random random = new Random();
    private static final int TRANSLATE_DURATION = 16000;//ms
    private int translateX, translateY;//px
    private static final int DELAY = 2 * 60 * 1000;//ms
    private boolean firstRun = true;
    private boolean isRunningAnimation = false;


    private void init(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(wallpaper);
        imageView.setAlpha(1f);
        imageView.setScaleX(1.2f);
        imageView.setScaleY(1.2f);
        addView(imageView);
        startScreen();
    }

    private Runnable startRunnable = new Runnable() {
        @Override
        public void run() {
            isRunningAnimation = true;
            step0();
        }
    };

    private void step0() {//init view1
        translateX = -random.nextInt(200);
        translateY = -random.nextInt(100);
        if (translateX > -100) {
            translateX = 200 + translateX;
        }
        if (translateY > -50) {
            translateY = 100 + translateY;
        }
        imageView.animate().translationXBy(-translateX)
                .withLayer()
                .translationYBy(-translateY)
                .setDuration(TRANSLATE_DURATION)
                .setInterpolator(new LinearInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunningAnimation) {
                            step1();
                        }
                    }
                }).start();
    }

    private void step1(){
        imageView.animate().translationXBy(translateX)
                .withLayer()
                .translationYBy(translateY)
                .setDuration(TRANSLATE_DURATION)
                .setInterpolator(new LinearInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunningAnimation) {
                            step0();
                        }
                    }
                }).start();
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public void startScreen() {
        if (firstRun) {
            post(startRunnable);
            firstRun = false;
        } else {
            postDelayed(startRunnable, DELAY);
        }
    }

    public void stopScreen() {
        getHandler().removeCallbacks(startRunnable);
        post(new Runnable() {
            @Override
            public void run() {
                isRunningAnimation = false;
                imageView.animate().alpha(0).setDuration(666).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        imageView.animate().cancel();
                    }
                }).start();
            }
        });
    }
}
