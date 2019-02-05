package com.example.waterbottle.client_side.client_model.client_product_model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;


import com.example.waterbottle.R;

import java.util.List;

public class client_product_listadepter extends ArrayAdapter<client_product_model> {
    List<client_product_model> productList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public client_product_listadepter(Context context, int resource, List<client_product_model> productList) {
        super(context, resource, productList);
        this.context = context;
        this.resource = resource;
        this.productList = productList;
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view

     /*   TextView textagentname = view.findViewById(R.id.);
        TextView texttotal= view.findViewById(R.id.textViewTeam);
      TextView textcollection = view.findViewById(R.id.textViewName);*/
        TextView tvnm = view.findViewById(R.id.tvproname);
        TextView tvprice = view.findViewById(R.id.pprice);
        TextView tvdetails = view.findViewById(R.id.prodetails);

        ImageView imgproduct = view.findViewById(R.id.imgproduct);



        //getting the hero of the specified position

        client_product_model product= productList.get(position);

        tvnm.setText(product.name);
        tvprice.setText(product.price);
        tvdetails.setText(product.details);


        imgproduct.setImageDrawable(context.getResources().getDrawable(product.getImage()));

        return view;
    }



    }

