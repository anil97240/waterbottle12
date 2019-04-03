package com.example.waterbottle.admin_agent_side.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;

import java.util.List;

public class agent_data extends ArrayAdapter<deliver_bottles> {
    List<deliver_bottles> delivryList;
    Context context;
    int resource;

    public agent_data(Context context, int resource, List<deliver_bottles> delivryList) {
        super(context, resource, delivryList);
        this.context = context;
        this.resource = resource;
        this.delivryList = delivryList;

    }
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);


        TextView tvagentname = view.findViewById(R.id.tvagentnm);
        TextView tvcollection = view.findViewById(R.id.tvcollection);
        TextView tvbottletotle = view.findViewById(R.id.tvbottletotle);

        deliver_bottles delorder = delivryList.get(position);

        tvagentname.setText(delorder.getAgent_email());
        tvcollection.setText(delorder.getTotal_amount());
        tvbottletotle.setText(delorder.getBotttles());

        return view;
    }

}
