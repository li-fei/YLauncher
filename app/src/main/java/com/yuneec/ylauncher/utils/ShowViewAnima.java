package com.yuneec.ylauncher.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class ShowViewAnima {

    public static void enterExitAppsView(boolean enter, View firstView, View appsView) {
        if (!enter && firstView.getVisibility() == View.VISIBLE){
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(enter ? firstView : appsView, "alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(enter ? appsView : firstView, "alpha", 0f, 1f);
        animatorSet.play(animator1).with(animator2);
        animatorSet.setDuration(700);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (enter) {
                    appsView.setVisibility(View.VISIBLE);
                } else {
                    firstView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (enter) {
                    firstView.setVisibility(View.GONE);
                } else {
                    appsView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void showView(View view1, View view2) {
        AlphaAnimation showAnimation = new AlphaAnimation(0f, 1f);
        showAnimation.setDuration(1000);
        AlphaAnimation hideAnimation = new AlphaAnimation(1f, 0f);
        hideAnimation.setDuration(1000);
        view1.startAnimation(showAnimation);
        view1.setVisibility(View.VISIBLE);
        view2.startAnimation(hideAnimation);
        view2.setVisibility(View.GONE);
    }

}
