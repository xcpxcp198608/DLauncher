package com.px.dlauncher.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;


public class Rotation {

    private static AnimatorSet animatorSet;

    static {
        animatorSet = new AnimatorSet();
    }

    public static void start (View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view , "rotation" , 0f ,-360f
                , 0f ,-360f , 0f ,-360f , 0f ,-360f , 0f ,-360f , 0f ,-360f , 0f ,-360f);
        animatorSet.play(objectAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();
    }

}
