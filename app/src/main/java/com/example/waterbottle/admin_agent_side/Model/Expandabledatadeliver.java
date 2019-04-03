package com.example.waterbottle.admin_agent_side.Model;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.order_delivered;
import com.firebase.client.Firebase;
import com.firebase.client.core.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expandabledatadeliver {

    private static final String TAG = "Mohit";
    int p;

    //the list values in the List of type hero
    ListView delivryListview;
    static List<String> football;
    //list to store uploads data
    static List<deliver_order> deliver_orderList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    deliver_order_adepter adapter;
    //Firebase Url Get INstance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    private DatabaseReference mDatabaseReference;
    public static HashMap<String, List<String>> getData() {
        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
           /*   for(int i=0;i<orderList.size();i++) {
                cricket = new ArrayList<String>();
                cricket.add(orderList.get(i).getBottle_type());
                cricket.add(orderList.get(i).getBottle_price());
                cricket.add(orderList.get(i).getOrder_date());
                cricket.add("England");
                cricket.add("South Africa");

            }*/

        final List<order> orderList;
        DatabaseReference mDatabaseReference;
        orderList = new ArrayList<>();
        deliver_orderList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        deliver_order upload = postSnapshot.getValue(deliver_order.class);
                        deliver_orderList.add(upload);

                    }

                    String[] uploads = new String[deliver_orderList.size()];
                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = deliver_orderList.get(i).getQR_code();
                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
                        football = new ArrayList<String>();
                        football.add(deliver_orderList.get(i).getCollected_Date());
                        football.add(deliver_orderList.get(i).getBottle_type());
                        football.add(deliver_orderList.get(i).getQR_code());
                        football.add(deliver_orderList.get(i).getPadding_amount());
                        football.add(deliver_orderList.get(i).getPayment_Method());
                        expandableListDetail.put(deliver_orderList.get(i).getCollected_Date(),football);

                    }
                    //displaying it to list
                    //select product for delete
                } catch (Exception e) {
                    Log.e(TAG, "New: " + e.getMessage());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });


        return expandableListDetail;
    }
}


