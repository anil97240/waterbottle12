package com.example.waterbottle.admin_agent_side.Model;


import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.core.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableListDataPump {
    ListView orderlistview;
    int p;
    static Context context;
    //list to store uploads data
    static List<order> orderList;
    DatabaseReference mDatabaseReference;
    ArrayList arrayList1 = new ArrayList<String>();
    ArrayList arrayList = new ArrayList<String>();
    static List<String> football;

    public static HashMap<String, List<String>> getData() {
        final HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        final List<order> orderList;
        DatabaseReference mDatabaseReference;
        orderList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_order");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        order upload = postSnapshot.getValue(order.class);
                        orderList.add(upload);
                    }
                    String[] uploads = new String[orderList.size()];
                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = orderList.get(i).getCustomer_id();
                        //  Log.e("abc", "ondatadd: " + uploads[i]);
                        football = new ArrayList<String>();
                        football.add(orderList.get(i).getOrder_date());
                     /*   football.add(orderList.get(i).getBottle_type());
                        football.add(orderList.get(i).getBottle_price());*/
                        football.add(orderList.get(i).getCustomer_id());
                    }
                } catch (Exception e) {
                    Log.e("data", "onDataChange : " + e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AAA", "onCancelled: " + databaseError.getMessage());
            }
        });

            expandableListDetail.put("data",football);

        return expandableListDetail;
    }
}

