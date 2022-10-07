package com.app.habit.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.habit.data.AppDatabaseManager;
import com.app.habit.data.model.Usage;
import com.app.habit.databinding.FragmentAnalyticsBinding;
import com.app.habit.helpers.Date;
import com.app.habit.logic.event.Event;
import com.app.habit.logic.usage.actions.Actions;
import com.app.habit.logic.usage.applications.ApplicationsPackage;
import com.app.habit.logic.usage.UsageAnalytic;
import com.app.habit.widget.AnalyticsListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AnalyticsFragment extends Fragment {

    private final String TAG = AnalyticsFragment.class.getName();
    private FragmentAnalyticsBinding _binding;

    private Event _onStartTakingDataEvent = new Event();
    private Event _onStopTakingDataEvent = new Event();

    private Event _onDayChangeEvent = new Event();

    private AnalyticsListAdapter _listAdapter = new AnalyticsListAdapter();

    private java.util.Date _analyticsDate;

    private void set_analyticsDate(java.util.Date newDate) {
        _analyticsDate = newDate;
        _onDayChangeEvent.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // enable binding
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _analyticsDate = Date.setHourMinSecMill_At0(Date.getCurrentTimeDate().getTime());

        // Set listeners for next day and previous day
        _binding.btnPreviousDay.setOnClickListener(view1 -> {
            set_analyticsDate(Date.getPreviousDay(_analyticsDate.getTime()));

        });
        _binding.btnNextDay.setOnClickListener(view1 -> {
            set_analyticsDate(Date.getNextDay(_analyticsDate.getTime()));
        });

        // Creating the ListView
        var ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        _binding.rvAnalytics.setLayoutManager(ll);
        _binding.rvAnalytics.setAdapter(_listAdapter);

        _onStartTakingDataEvent.subscribe(() -> {
            toggleProgressBar(true);
            //toggleAnalyticsTopBar(false);
        });

        _onStopTakingDataEvent.subscribe(() -> {
            toggleProgressBar(false);
            //toggleAnalyticsTopBar(true);
        });

        _onDayChangeEvent.subscribe(this::retrieveAndShowDataFromDatabase);

        retrieveAndShowDataFromDatabase();
    }


    private void toggleProgressBar(boolean showProgressBar) {
        _binding.pbAnalytics.setVisibility( (showProgressBar) ? View.VISIBLE : View.GONE );
        _binding.rvAnalytics.setVisibility( (showProgressBar) ? View.GONE : View.VISIBLE);
    }

    private void toggleAnalyticsTopBar(boolean showAnalyticsTopBar) {
        _binding.llAnalyticsTopBar.setVisibility( (showAnalyticsTopBar) ? View.VISIBLE : View.GONE );
    }

    @SuppressLint("SimpleDateFormat")
    private void retrieveAndShowDataFromDatabase() {

        _onStartTakingDataEvent.execute();

        var executor = Executors.newSingleThreadExecutor();

        // Execute
        executor.execute(() -> {

            // Get Data  from DB  on a different thread  (executor)
            var usage = AppDatabaseManager.getDatabase(getContext()).UsagesDao().get(_analyticsDate);

            // Logging the data retrieved
            Log.i(TAG, Usage.toString(usage, getContext()));

            // Show data retrieved on  the Analytics list  using main thread executor (UI Thread)
            getActivity().getMainExecutor().execute(() -> {
                _binding.tvDate.setText(new  SimpleDateFormat("dd/MM/yyyy").format(_analyticsDate));
                _listAdapter.setData(createUsageAnalyticList(usage));
                _onStopTakingDataEvent.execute();
            });
        });
    }


    private List<UsageAnalytic> createUsageAnalyticList(Usage usage) {

        if (usage == null)
            usage = Usage.emptyUsage();

        var instagram = new UsageAnalytic(
                ApplicationsPackage.INSTAGRAM_NAME,
                UsageAnalytic.APPLICATION,
                usage.instagram,
                usage.lastTimeUsed_instagram,
                ApplicationsPackage.INSTAGRAM_ICON_ID,
                usage.usageDay.getTime()
        );

        var facebook = new UsageAnalytic(
                ApplicationsPackage.FACEBOOK_NAME,
                UsageAnalytic.APPLICATION,
                usage.facebook,
                usage.lastTimeUsed_facebook,
                ApplicationsPackage.FACEBOOK_ICON_ID,
                usage.usageDay.getTime()
        );

        var youtube = new UsageAnalytic(
                ApplicationsPackage.YOUTUBE_NAME,
                UsageAnalytic.APPLICATION,
                usage.youtube,
                usage.lastTimeUsed_youtube,
                ApplicationsPackage.YOUTUBE_ICON_ID,
                usage.usageDay.getTime()
        );

        var pinterest = new UsageAnalytic(
                ApplicationsPackage.PINTEREST_NAME,
                UsageAnalytic.APPLICATION,
                usage.pinterest,
                usage.lastTimeUsed_pinterest,
                ApplicationsPackage.PINTEREST_ICON_ID,
                usage.usageDay.getTime()
        );

        var linkedin = new UsageAnalytic(
                ApplicationsPackage.LINKEDIN_NAME,
                UsageAnalytic.APPLICATION,
                usage.linkedin,
                usage.lastTimeUsed_linkedin,
                ApplicationsPackage.LINKEDIN_ICON_ID,
                usage.usageDay.getTime()
        );

        var twitter = new UsageAnalytic(
                ApplicationsPackage.TWITTER_NAME,
                UsageAnalytic.APPLICATION,
                usage.twitter,
                usage.lastTimeUsed_twitter,
                ApplicationsPackage.TWITTER_ICON_ID,
                usage.usageDay.getTime()
        );

        var driving = new UsageAnalytic(
                Actions.DRIVING_NAME,
                UsageAnalytic.ACTION,
                usage.driving,
                Actions.DRIVING_ICON_ID,
                usage.usageDay.getTime()
        );

        var moving = new UsageAnalytic(
                Actions.MOVING_NAME,
                UsageAnalytic.ACTION,
                usage.moving,
                Actions.MOVING_ICON_ID,
                usage.usageDay.getTime()
        );

        var beSit = new UsageAnalytic(
                Actions.BE_SIT_NAME,
                UsageAnalytic.ACTION,
                usage.beSit,
                Actions.BE_SIT_ICON_ID,
                usage.usageDay.getTime()
        );

        var usageAnalyticList = new ArrayList<UsageAnalytic>();
        usageAnalyticList.add(instagram);
        usageAnalyticList.add(facebook);
        usageAnalyticList.add(youtube);
        usageAnalyticList.add(pinterest);
        usageAnalyticList.add(linkedin);
        usageAnalyticList.add(twitter);
        usageAnalyticList.add(driving);
        usageAnalyticList.add(moving);
        usageAnalyticList.add(beSit);

        Log.i(TAG, "created  usage list");
        return usageAnalyticList;
    }

}