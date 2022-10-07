package com.app.habit.helpers;

import android.view.View;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class SpringAnimator {


    public static SpringAnimation CreateSpringAnimationSlBh(View view, DynamicAnimation.ViewProperty springViewProperty, float finalPosition) {
        return CreateSpringAnimation(view, springViewProperty, finalPosition, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    }

    public static SpringAnimation CreateSpringAnimationSlBl(View view, DynamicAnimation.ViewProperty springViewProperty, float finalPosition) {
        return CreateSpringAnimation(view, springViewProperty, finalPosition, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_LOW_BOUNCY);
    }

    private static SpringAnimation CreateSpringAnimation(View view, DynamicAnimation.ViewProperty springViewProperty, float finalPosition, float springForceStiffness, float springForceBouncy) {
        SpringAnimation springAnimation =  new SpringAnimation(view,  springViewProperty, finalPosition);
        springAnimation.getSpring()
                .setStiffness(springForceStiffness)
                .setDampingRatio(springForceBouncy);
        return springAnimation;
    }
}
