package com.app.habit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.SpringAnimation;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.app.habit.R;
import com.app.habit.databinding.ActivityIntro1Binding;
import com.app.habit.helpers.ActivityChanger;
import com.app.habit.helpers.SpringAnimator;

public class Intro1Activity extends AppCompatActivity {

    private @NonNull ActivityIntro1Binding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        // enable binding
        _binding  = ActivityIntro1Binding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        // initialize listeners for views
        initializeListeners();
    }


    private void initializeListeners() {
        _binding.btnStartHere.setOnClickListener(l -> {
            ActivityChanger.ChangeActivityAndFinish(this, Intro2Activity.class);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeAnimation();
    }


    private void makeAnimation() {

        _binding.tvTo.setVisibility(View.INVISIBLE);
        _binding.tvHabit.setVisibility(View.INVISIBLE);
        _binding.tvFirstTimeHere.setRotationX(90);
        _binding.tvLetsLook.setRotationX(90);
        _binding.btnStartHere.setRotationX(90);
        _binding.btnStartHere.setEnabled(false);

        SpringAnimation tvFirstTimeHereAnim =
                SpringAnimator.CreateSpringAnimationSlBh(_binding.tvFirstTimeHere, SpringAnimation.ROTATION_X,  0);

        SpringAnimation tvLetsLookAnim =
                SpringAnimator.CreateSpringAnimationSlBh(_binding.tvLetsLook, SpringAnimation.ROTATION_X,  0);

        SpringAnimation btnStart =
                SpringAnimator.CreateSpringAnimationSlBh(_binding.btnStartHere, SpringAnimation.ROTATION_X,  0);

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation1.setDuration(600);
        alphaAnimation1.setFillAfter(true);

        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation2.setDuration(600);
        alphaAnimation2.setFillAfter(true);

        AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation3.setDuration(600);
        alphaAnimation3.setFillAfter(true);

        _binding.tvWelcome.animate()
                .withStartAction(() -> {
                    _binding.tvWelcome.startAnimation(alphaAnimation1);
                }).setDuration(600)
                .withEndAction(() -> {
                    _binding.tvTo.setVisibility(View.VISIBLE);
                    _binding.tvTo.animate()
                            .withStartAction(() -> {
                                _binding.tvTo.startAnimation(alphaAnimation2);
                            }).setDuration(600)
                            .withEndAction(() -> {
                                _binding.tvHabit.setVisibility(View.VISIBLE);
                                _binding.tvHabit.animate()
                                        .withStartAction(() -> {
                                            _binding.tvHabit.startAnimation(alphaAnimation3);
                                        }).setDuration(600)
                                        .withEndAction(() -> {
                                            _binding.tvFirstTimeHere.animate()
                                                    .withStartAction(() -> {
                                                        tvFirstTimeHereAnim.start();
                                                    }).setDuration(600)
                                                    .withEndAction(() -> {
                                                        _binding.tvLetsLook.animate()
                                                                .withStartAction(() -> {
                                                                    tvLetsLookAnim.start();
                                                                }).setDuration(600)
                                                                .withEndAction(() -> {
                                                                    _binding.btnStartHere.animate()
                                                                            .withStartAction(() -> {
                                                                                btnStart.start();
                                                                            }).setDuration(300)
                                                                            .withEndAction(() -> {
                                                                                _binding.btnStartHere.setEnabled(true);
                                                                            })
                                                                            .start();
                                                                })
                                                                .start();
                                                    })
                                                    .start();
                                        })
                                        .start();
                            })
                            .start();
                })


                .start();
    }
}