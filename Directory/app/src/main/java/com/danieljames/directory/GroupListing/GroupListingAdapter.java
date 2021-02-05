package com.danieljames.directory.GroupListing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.danieljames.directory.GroupHome.GroupHome;
import com.danieljames.directory.R;
import com.danieljames.directory.model.GroupsData;

public class GroupListingAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Activity activity;

    public GroupListingAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return GroupsData.groupsData.groups.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_group_listing_row, null);
        }
        TextView valueTextView = (TextView) view.findViewById(R.id.group_name);
        valueTextView.setText(GroupsData.groupsData.groups.get(position).groupName);
        TextView keyTextView = (TextView) view.findViewById(R.id.group_summary);
        keyTextView.setText(GroupsData.groupsData.groups.get(position).summary);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(activity, GroupHome.class);
                intent.putExtra("groupPosition", position);
                activity.startActivity(intent);
            }
        });
        return view;
    }
}
