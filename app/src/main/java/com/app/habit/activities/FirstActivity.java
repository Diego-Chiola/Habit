package com.app.habit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.app.habit.logic.SettingsManager;
import com.app.habit.R;
import com.app.habit.databinding.ActivityFirstBinding;
import com.app.habit.helpers.ActivityChanger;

public class FirstActivity extends AppCompatActivity {

    private @NonNull ActivityFirstBinding _binding;
    private final int WAITING_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // enable binding
        _binding  = ActivityFirstBinding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        new Handler().postDelayed(() -> {

            // check if is the first time  that  the user enter  in the app
            boolean isFirstTime = SettingsManager.getFirstTimeEnterSharedPreferences(this)
                    .getBoolean(SettingsManager.FIRST_ENTER_isFirstEnter, true);

            if (isFirstTime)
                ActivityChanger.ChangeActivityAndFinish(this, Intro1Activity.class);
            else
                ActivityChanger.ChangeActivityAndFinish(this, MainActivity.class);


        }, WAITING_TIME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        _binding.vBottomRect.animate()
                .setDuration(1000)
                .withEndAction(() -> {
                    _binding.vBottomRect.animate()
                            .setDuration(1000)
                            .translationXBy(-240)
                            .start();
                })
                .start();

        _binding.vTopRect.animate()
                .setDuration(1000)
                .withEndAction(() -> {
                    _binding.vTopRect.animate()
                            .setDuration(1000)
                            .translationX(240)
                            .start();
                })
                .start();
    }
}