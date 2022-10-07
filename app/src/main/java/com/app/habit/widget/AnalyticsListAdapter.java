package com.app.habit.widget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.habit.R;
import com.app.habit.activities.AnalyticsMoreActivity;
import com.app.habit.activities.MainActivity;
import com.app.habit.fragments.AnalyticsFragment;
import com.app.habit.helpers.Converter;
import com.app.habit.logic.usage.UsageAnalytic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalyticsListAdapter extends RecyclerView.Adapter<AnalyticsListAdapter.MyViewHolder> {

    private final String TAG = AnalyticsListAdapter.class.getName();

    private List<UsageAnalytic> _usaUsageAnalyticList = new ArrayList<>();
    private ViewGroup _parent;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _parent = parent;
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.analytics_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        var currentItem = _usaUsageAnalyticList.get(position);

        // Getting the widgets  of the analytics card
        var icon = (ImageView) holder.itemView.findViewById(R.id.iv_analytics_icon_item_list);
        var title = (TextView) holder.itemView.findViewById(R.id.analytics_card_title);
        var timeUsage = (TextView) holder.itemView.findViewById(R.id.analytics_card_time_usage);
        var timeUsageUnit = (TextView) holder.itemView.findViewById(R.id.analytics_card_time_usage_unit);
        var lastTimeOpen = (TextView) holder.itemView.findViewById(R.id.analytics_card_last_time_open);
        var seeMoreButton = (Button) holder.itemView.findViewById(R.id.btn_analytics_item_see_more);

        // Set the widgets
        icon.setImageResource(currentItem.get_icon());
        title.setText(currentItem.get_name());

        var timeUsageConverted = Converter.fromLongToString_TimeElapsed(currentItem.get_usageTime(), _parent.getContext());
        timeUsage.setText(timeUsageConverted.getKey());
        timeUsageUnit.setText(timeUsageConverted.getValue());

        if (currentItem.get_lastTimeUsage() == 0) {
            lastTimeOpen.setText(_parent.getResources().getString(R.string.not_used));
        } else {
            var timeFormat = Converter.fromLongToString_dayHourAndMinutes(currentItem.get_lastTimeUsage(), _parent.getContext());
            lastTimeOpen.setText(timeFormat);
        }

        seeMoreButton.setOnClickListener(view -> {
            var intent = new Intent(_parent.getContext(), AnalyticsMoreActivity.class);

            intent.putExtra(AnalyticsMoreActivity.DATE, currentItem.get_date());
            intent.putExtra(AnalyticsMoreActivity.ICON, currentItem.get_icon());
            intent.putExtra(AnalyticsMoreActivity.NAME, currentItem.get_name());
            intent.putExtra(AnalyticsMoreActivity.USAGE_TIME, currentItem.get_usageTime());
            intent.putExtra(AnalyticsMoreActivity.LAST_USAGE_TIME, currentItem.get_lastTimeUsage());
            intent.putExtra(AnalyticsMoreActivity.TYPE, currentItem.get_type());

            _parent.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return _usaUsageAnalyticList.size();
    }


    public void setData(List<UsageAnalytic> usageAnalyticList) {
        Log.i(TAG, "data set");
        _usaUsageAnalyticList = usageAnalyticList;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
