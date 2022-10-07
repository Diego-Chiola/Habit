package com.app.habit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.app.habit.R;
import com.app.habit.data.AppDatabaseManager;
import com.app.habit.data.model.Usage;
import com.app.habit.databinding.ActivityAnalyticsMoreBinding;
import com.app.habit.helpers.Converter;
import com.app.habit.helpers.Date;
import com.app.habit.logic.usage.actions.Actions;
import com.app.habit.logic.usage.applications.ApplicationsPackage;
import com.app.habit.logic.usage.UsageAnalytic;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsMoreActivity extends AppCompatActivity {


    private static final String TAG = AnalyticsMoreActivity.class.getName();

    public static final String DATE =  "date";
    public static final String ICON =  "icon";
    public static final String NAME =  "name";
    public static final String USAGE_TIME =  "usage_time";
    public static final String LAST_USAGE_TIME =  "last_usage_time";
    public static final String TYPE =  "type";

    private ActivityAnalyticsMoreBinding _binding;
    private UsageAnalytic _itemUsageAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_more);

        // enable binding
        _binding  = ActivityAnalyticsMoreBinding.inflate(getLayoutInflater());
        View view = _binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //  get  the application/action usages from the bundle
            _itemUsageAnalytics = new UsageAnalytic(
                    extras.getString(NAME),
                    extras.getInt(TYPE),
                    extras.getLong(USAGE_TIME),
                    extras.getLong(LAST_USAGE_TIME),
                    extras.getInt(ICON),
                    extras.getLong(DATE)
            );

            // set the application/action name title
            _binding.tvAnalyticsSeeMoreTitle.setText(_itemUsageAnalytics.get_name());

            // set the application/action icon
            _binding.ivAnalyticsSeeMoreIcon.setImageResource(_itemUsageAnalytics.get_icon());


            _binding.chartAnalyticsSeeMoreLast7days.setDrawHoleEnabled(true);
            _binding.chartAnalyticsSeeMoreLast7days.setUsePercentValues(true);
            _binding.chartAnalyticsSeeMoreLast7days.getDescription().setEnabled(false);
            _binding.chartAnalyticsSeeMoreLast7days.setEntryLabelColor(Color.BLACK);
            _binding.chartAnalyticsSeeMoreLast7days.setEntryLabelTextSize(0f);
            var legend = _binding.chartAnalyticsSeeMoreLast7days.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setEnabled(true);

            // set other  information charts and other  (if no data from last 7 days display no  info)
            new Thread(() -> {
                var last7daysUsage = AppDatabaseManager.getDatabase(this)
                        .UsagesDao()
                        .getInInterval(
                                Date.removeDayFromTime(_itemUsageAnalytics.get_date(), 7),
                                new java.util.Date(_itemUsageAnalytics.get_date())
                        );

                if (last7daysUsage == null || last7daysUsage.size() == 0) {
                    getMainExecutor().execute(() -> {
                        _binding.tvAnalyticsSeeMoreNoInfo.setVisibility(View.VISIBLE);
                        _binding.llAnalyticsSeeMoreOtherInfo.setVisibility(View.GONE);
                    });
                } else {
                    getMainExecutor().execute(() -> {
                        setupPieChart(last7daysUsage);
                    });
                }
            }).start();
        }
    }

    private void setupPieChart(List<Usage> last7daysUsage) {
        if(_itemUsageAnalytics.get_type() == UsageAnalytic.APPLICATION) {
            setupApplications(last7daysUsage);
        } else {
            setupActions(last7daysUsage);
        }
    }


    private void setupApplications(List<Usage> last7daysUsage) {
        var usageMap = sumAllApplicationUsage(last7daysUsage);

        boolean isUsageMap_AllZeros = true;
        for (var usageTime : usageMap.values()) {
            if (usageTime != 0) {
                isUsageMap_AllZeros = false;
                break;
            }
        }

        if (isUsageMap_AllZeros){
            _binding.tvAnalyticsSeeMoreNoInfo.setText(R.string.no_app_used_in_last_7_days);
            _binding.tvAnalyticsSeeMoreNoInfo.setVisibility(View.VISIBLE);
            _binding.llAnalyticsSeeMoreOtherInfo.setVisibility(View.GONE);
            return;
        }

        setupApplicationPieChart(usageMap);


        var appPackage = ApplicationsPackage.packageMap.get(_itemUsageAnalytics.get_name());
        var timeUsed = usageMap.get(appPackage);

        var timeConvert = Converter.fromLongToString_TimeElapsed(timeUsed, this);
        _binding.tvTotalTimeLast7DaysTime.setText(timeConvert.getKey());
        _binding.tvTotalTimeLast7DaysUnit.setText(timeConvert.getValue());

        var avg = timeUsed  / 7;
        var avgTimeConvert  =  Converter.fromLongToString_TimeElapsed(avg, this);
        _binding.tvTotalTimeLast7DaysTimeAvg.setText((avgTimeConvert.getKey().equals(getString(R.string.not_used)) && usageMap.get(appPackage) != 0) ? getString(R.string.not_relevant) : avgTimeConvert.getKey());
        _binding.tvTotalTimeLast7DaysUnitAvg.setText(avgTimeConvert.getValue());
    }

    private void setupActions(List<Usage> last7daysUsage) {
        var usageMap = sumAllActionUsage(last7daysUsage);

        boolean isUsageMap_AllZeros = true;
        for (var usageTime : usageMap.values()) {
            if (usageTime != 0) {
                isUsageMap_AllZeros = false;
                break;
            }
        }

        if (isUsageMap_AllZeros){
            _binding.tvAnalyticsSeeMoreNoInfo.setText(R.string.no_actions_used_in_last_7_days);
            _binding.tvAnalyticsSeeMoreNoInfo.setVisibility(View.VISIBLE);
            _binding.llAnalyticsSeeMoreOtherInfo.setVisibility(View.GONE);
            return;
        }

        setupActionPieChart(usageMap);
    }



    private void setupApplicationPieChart(Map<String, Long> usageMap) {


        List<PieEntry> pieEntries =  new ArrayList<>();
        for (var appPackage : ApplicationsPackage.PackageArray) {
            if(usageMap.get(appPackage) !=  null && usageMap.get(appPackage) != 0)
                pieEntries.add(new PieEntry(usageMap.get(appPackage) /  60000L, ApplicationsPackage.nameMap.get(appPackage)));
        }

        var colors = new ArrayList<Integer>();
        for (var color : ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (var color : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(_binding.chartAnalyticsSeeMoreLast7days));
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.BLACK);

        _binding.chartAnalyticsSeeMoreLast7days.setData(data);
        _binding.chartAnalyticsSeeMoreLast7days.invalidate();
        _binding.chartAnalyticsSeeMoreLast7days.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupActionPieChart(Map<String, Long> usageMap) {
        List<PieEntry> pieEntries =  new ArrayList<>();
        for (var action : Actions.actionsArray) {
            if(usageMap.get(action) !=  null && usageMap.get(action) != 0)
                pieEntries.add(new PieEntry(usageMap.get(action) /  60000L, ApplicationsPackage.nameMap.get(action)));
        }

        var colors = new ArrayList<Integer>();
        for (var color : ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (var color : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(_binding.chartAnalyticsSeeMoreLast7days));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        _binding.chartAnalyticsSeeMoreLast7days.setData(data);
        _binding.chartAnalyticsSeeMoreLast7days.invalidate();
        _binding.chartAnalyticsSeeMoreLast7days.animateY(1400, Easing.EaseInOutQuad);
    }

    private Map<String, Long> sumAllApplicationUsage(List<Usage> last7daysUsage) {
        var usageMap = new HashMap<String, Long>();
        for(var appPackage : ApplicationsPackage.PackageArray) {
            usageMap.put(appPackage, 0L);
        }

        for (var usage : last7daysUsage) {

            var auxUsageMap = Usage.toMapUsages(usage);
            for (var appPackage : ApplicationsPackage.PackageArray) {

                var appTimeUsage = auxUsageMap.get(appPackage);
                if (appTimeUsage != null) {
                    var auxTimeUsage = usageMap.get(appPackage);
                    usageMap.put(appPackage, auxTimeUsage + appTimeUsage);
                }
            }
        }
        return usageMap;
    }

    private Map<String, Long> sumAllActionUsage(List<Usage> last7daysUsage) {
        var usageMap = new HashMap<String, Long>();
        for(var action : Actions.actionsArray) {
            usageMap.put(action, 0L);
        }

        for (var usage : last7daysUsage) {

            var auxUsageMap = Usage.toMapUsages(usage);
            for (var action : Actions.actionsArray) {

                var appTimeUsage = auxUsageMap.get(action);
                if (appTimeUsage != null) {
                    var auxTimeUsage = usageMap.get(action);
                    usageMap.put(action, auxTimeUsage + appTimeUsage);
                }
            }
        }
        return usageMap;
    }
}