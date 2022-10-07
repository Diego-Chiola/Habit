package com.app.habit.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.habit.R;
import com.app.habit.data.AppDatabaseManager;
import com.app.habit.data.model.Usage;
import com.app.habit.databinding.FragmentHomeBinding;
import com.app.habit.helpers.Converter;
import com.app.habit.helpers.Date;
import com.app.habit.logic.usage.actions.Actions;
import com.app.habit.logic.usage.applications.ApplicationsPackage;
import com.app.habit.logic.user.UserManager;
import com.google.android.material.imageview.ShapeableImageView;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding _binding;

    public HomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // enable binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // load full name
        UserManager.get().update();
        _binding.tvFullName.setText(UserManager.get().get_fullName());

        new Thread(() -> {
            var usage = AppDatabaseManager.getDatabase(getContext())
                    .UsagesDao().get(Date.getTime_atMidnight());

            if(usage == null)
                usage = new Usage();

            Usage finalUsage = usage;
            getActivity().getMainExecutor().execute(() -> {
                createCards(finalUsage);
                setTotalTimeUsing(finalUsage);
            });

        }).start();

    }


    private void setTotalTimeUsing(Usage usage) {
        long phoneTotalTimeUsage = 0;
        phoneTotalTimeUsage += usage.instagram;
        phoneTotalTimeUsage += usage.facebook;
        phoneTotalTimeUsage += usage.youtube;
        phoneTotalTimeUsage += usage.pinterest;
        phoneTotalTimeUsage += usage.linkedin;
        phoneTotalTimeUsage += usage.twitter;

        long actionTotalTimeUsage = 0;
        actionTotalTimeUsage += usage.driving;
        actionTotalTimeUsage += usage.moving;
        actionTotalTimeUsage += usage.beSit;

        if (phoneTotalTimeUsage == 0) {
            _binding.tvDayTimeUsingPhone.setText("0");
            _binding.tvUnitDayTimeUsingPhone.setText("min");
        } else {
            var timeFormat = Converter.fromLongToString_TimeElapsed(phoneTotalTimeUsage, getContext());
            _binding.tvDayTimeUsingPhone.setText(timeFormat.getKey());
            _binding.tvUnitDayTimeUsingPhone.setText(timeFormat.getValue());
        }

        if (actionTotalTimeUsage == 0) {
            _binding.tvDayTimeOtherActions.setText("0");
            _binding.tvUnitDayTimeOtherActions.setText("min");
        } else {
            var timeFormat = Converter.fromLongToString_TimeElapsed(actionTotalTimeUsage, getContext());
            _binding.tvDayTimeOtherActions.setText(timeFormat.getKey());
            _binding.tvUnitDayTimeOtherActions.setText(timeFormat.getValue());
        }
    }


    private void createCards(Usage usage) {
        var usageMap =  Usage.toMapUsages(usage);

        for (var app : ApplicationsPackage.PackageArray) {
            var appCard = getLayoutInflater().inflate(R.layout.main_activity_card, null);

            var icon = (ShapeableImageView) appCard.findViewById(R.id.main_card_icon);
            icon.setImageResource(ApplicationsPackage.iconMap.get(app));

            var title = (TextView) appCard.findViewById(R.id.main_card_app_name);
            title.setText(ApplicationsPackage.nameMap.get(app));

            var timeUsage = (TextView) appCard.findViewById(R.id.main_card_time_usage);
            var timeUnit = (TextView) appCard.findViewById(R.id.main_card_time_unit);
            var timeFormatted = Converter.fromLongToString_TimeElapsed(
                    usageMap.get(app),
                    getContext()
            );
            timeUsage.setText(timeFormatted.getKey());
            timeUnit.setText(timeFormatted.getValue());

            _binding.llMainAppUsages.addView(appCard);
        }

        for (var action : Actions.actionsArray) {
            var actionCard = getLayoutInflater().inflate(R.layout.main_activity_card, null);

            var icon = (ShapeableImageView) actionCard.findViewById(R.id.main_card_icon);
            icon.setImageResource(Actions.iconMap.get(action));

            var title = (TextView) actionCard.findViewById(R.id.main_card_app_name);
            title.setText(Actions.nameMap.get(action));

            var titleSecond = (TextView) actionCard.findViewById(R.id.main_card_second_title);
            titleSecond.setText(R.string.time_done);

            var timeUsage = (TextView) actionCard.findViewById(R.id.main_card_time_usage);
            var timeUnit = (TextView) actionCard.findViewById(R.id.main_card_time_unit);
            var timeFormatted = Converter.fromLongToString_TimeElapsed(
                    usageMap.get(action),
                    getContext()
            );
            timeUsage.setText((timeFormatted.getKey().equals(getString(R.string.not_used))) ? "0" : timeFormatted.getKey());
            timeUnit.setText((timeFormatted.getKey().equals(getString(R.string.not_used))) ? "min" : timeFormatted.getValue());

            _binding.llMainActionUsages.addView(actionCard);
        }
    }

}