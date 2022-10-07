package com.app.habit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.SpringAnimation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.app.habit.R;
import com.app.habit.databinding.ActivityIntro2Binding;
import com.app.habit.helpers.ActivityChanger;
import com.app.habit.helpers.SpringAnimator;

public class Intro2Activity extends AppCompatActivity {

    private ActivityIntro2Binding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);

        // enable binding
        _binding  = ActivityIntro2Binding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);
        
        _binding.continuee.setOnClickListener(l -> ActivityChanger.ChangeActivityAndFinish(this, RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        float cardView1FinalPosition = _binding.cardView1.getTranslationX();
        float cardView2FinalPosition = _binding.cardView2.getTranslationX();
        float imageGroup1FinalPosition = _binding.llImageGroup1.getTranslationX();
        float imageGroup2FinalPosition = _binding.llImageGroup2.getTranslationX();
        float btnGoToHabitFinalPosition = _binding.continuee.getTranslationX();

        _binding.cardView1.setTranslationX(-1000);
        _binding.llImageGroup1.setTranslationX(1000);
        _binding.cardView2.setTranslationX(1000);
        _binding.llImageGroup2.setTranslationX(-1000);
        _binding.continuee.setTranslationX(1000);


        new Handler().postDelayed(() -> {
            SpringAnimation cardView1Anim = SpringAnimator.CreateSpringAnimationSlBl(_binding.cardView1, SpringAnimation.TRANSLATION_X, cardView1FinalPosition);
            SpringAnimation cardView2Anim = SpringAnimator.CreateSpringAnimationSlBl(_binding.cardView2, SpringAnimation.TRANSLATION_X, cardView2FinalPosition);
            SpringAnimation imageGroup1Anim = SpringAnimator.CreateSpringAnimationSlBl(_binding.llImageGroup1, SpringAnimation.TRANSLATION_X, imageGroup1FinalPosition);
            SpringAnimation imageGroup2Anim = SpringAnimator.CreateSpringAnimationSlBl(_binding.llImageGroup2, SpringAnimation.TRANSLATION_X, imageGroup2FinalPosition);
            SpringAnimation btnGoToHabitAnim = SpringAnimator.CreateSpringAnimationSlBl(_binding.continuee, SpringAnimation.TRANSLATION_X, btnGoToHabitFinalPosition);

            _binding.cardView1.animate()
                    .withStartAction(() -> {
                        cardView1Anim.start();
                        imageGroup1Anim.start();
                    }).setDuration(800)
                    .withEndAction(() -> {
                        cardView2Anim.start();
                        imageGroup2Anim.start();
                        btnGoToHabitAnim.start();
                    })
                    .start();
        },800);
    }
}