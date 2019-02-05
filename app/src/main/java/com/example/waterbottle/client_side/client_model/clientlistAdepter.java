package com.example.waterbottle.client_side.client_model;


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

public class clientlistAdepter extends ArrayAdapter<client> {

    List<client> clientList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public clientlistAdepter(Context context, int resource, List<client> clientList) {
        super(context, resource, clientList);
        this.context = context;
        this.resource = resource;
        this.clientList = clientList;
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
        TextView tvnm = view.findViewById(R.id.tvclientnm);
        TextView tvmob = view.findViewById(R.id.tvmobno);
        TextView tvbar = view.findViewById(R.id.tvbarcode);
        TextView tvadd = view.findViewById(R.id.tvaddress);
        ImageView imgclient = view.findViewById(R.id.imgclient);



        //getting the hero of the specified position
        client client= clientList.get(position);

        //adding values to the list item

       /* textagentname.setText(agent.);
        texttotal.setText(agent.getTeam());
        textcollection.setText(agent.getName());*/
        tvnm.setText(client.name);
        tvmob.setText(client.no);
        tvbar.setText(client.barcode);
        tvadd.setText(client.address);
        imgclient.setImageDrawable(context.getResources().getDrawable(client.getImage()));
        //adding a click listener to the button to remove item from the list
      /*  buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                removeAgent(position);
            }
        });*/

        //finally returning the view
        return view;
    }

  /*  //this method will remove the item from the list
    private void removeAgent(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                agentList.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}*/
}
