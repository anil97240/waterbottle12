package com.example.waterbottle.admin_agent_side.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.waterbottle.R;

import java.util.List;

public class order_adepter extends ArrayAdapter<order> {

    List<order> orderList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;


    public order_adepter(Context context, int resource, List<order> orderList) {
        super(context, resource, orderList);
        this.context = context;
        this.resource = resource;
        this.orderList = orderList;
    }


    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);


        TextView tvbottletype = view.findViewById(R.id.tvbottle_type);
        TextView tvcustomer = view.findViewById(R.id.tvcustomer_name);
        TextView tvbottle = view.findViewById(R.id.tvbottle);


        order ord = orderList.get(position);

        tvbottletype.setText(ord.getOrder_date());
        tvcustomer.setText(ord.getCustomer_name());
        tvbottle.setText(ord.getBotttle_qty());

        return view;
    }


}





