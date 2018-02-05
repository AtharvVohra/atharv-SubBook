package com.example.atharv.atharv_assignment1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atharv on 2/4/2018.
 */

public class ListViewAdapter extends ArrayAdapter <Subscription>{


    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        // Get the data item for this position
        Subscription sub = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view  == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView charge = (TextView) view.findViewById(R.id.charge);
        TextView comments = (TextView) view.findViewById(R.id.comments);
        TextView date = (TextView) view.findViewById(R.id.date);

        name.setText(sub.getName());
        charge.setText(sub.getCharge());
        comments.setText(sub.getComment());
        date.setText(sub.getStartDate());

        //TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        //tvName.setText(user.name);
        //tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return view;

    }

}
