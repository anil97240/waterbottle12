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

public class deliver_order_adepter extends ArrayAdapter<deliver_order> {


    List<deliver_order> delivryList;
    Context context;

    int resource;

    public deliver_order_adepter(Context context, int resource, List<deliver_order> delivryList) {
        super(context, resource, delivryList);
        this.context = context;
        this.resource = resource;
        this.delivryList = delivryList;
    }


    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);


        TextView tvamount = view.findViewById(R.id.tvamount);
        TextView tvcustomer = view.findViewById(R.id.tvcustomer_name);
        TextView tvpadding = view.findViewById(R.id.tvpadding);


        deliver_order delorder = delivryList.get(position);


        tvamount.setText(delorder.getAmount_collected());
        tvcustomer.setText(delorder.getQR_code());
        tvpadding.setText(delorder.getPadding_amount());


        return view;
    }


}

