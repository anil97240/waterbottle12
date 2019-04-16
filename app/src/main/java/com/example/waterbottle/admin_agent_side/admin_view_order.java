package com.example.waterbottle.admin_agent_side;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.example.waterbottle.MainActivity;
import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.CustomExpandableListAdapter;
import com.example.waterbottle.admin_agent_side.Model.order;
import com.example.waterbottle.admin_agent_side.Model.order_adepter;
import com.example.waterbottle.admin_agent_side.Model.orderdetails;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class admin_view_order extends AppCompatActivity {
    CurvesLoader curvesLoader;

    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListViewAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> childItem;
    int counter = 0;

    private static final String TAG = "Mohit";
    order_adepter adapter;
    //the list values in the List of type hero
    ListView orderlistview;
    int p;
    //list to store uploads data
    List<order> orderList;
    List<orderdetails> orderdetailList;
    ArrayList arrayList = new ArrayList<String>();
    //Firebase Url Get Instance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    private DatabaseReference mDatabaseReference;
    //expandlistview
    CustomExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_admin_view_order);

        curvesLoader = findViewById(R.id.curvesLoader);
        // curvesLoader.setVisibility(View.GONE);

        SharedPreferences prfs = getSharedPreferences("auth", MODE_PRIVATE);
        String authname = prfs.getString("authname", "");
        expandableListView = findViewById(R.id.expandableListView);
        if (authname == "") {
            Intent intent = new Intent(admin_view_order.this, agent_login.class);
            SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(intent);
            finish();
        } else {

        }

        SetStandardGroups();
        customExpandableListViewAdapter = new CustomExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(customExpandableListViewAdapter);
        //initializing objects
        orderList = new ArrayList<>();
        orderdetailList = new ArrayList<>();
        orderlistview = (ListView) findViewById(R.id.orderlistview);
        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_order");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        order upload = postSnapshot.getValue(order.class);
                        orderList.add(upload);
                        arrayList.add(postSnapshot.getKey());
                    }
                    // curvesLoader.setVisibility(View.GONE);
                    String[] uploads = new String[orderList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = orderList.get(i).getCustomer_id();
                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
                    }
                    //displaying it to list
                    if (orderList.equals("")) {
                        Toast.makeText(admin_view_order.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new order_adepter(getApplicationContext(), R.layout.order_listview, orderList);
                        orderlistview.setAdapter(adapter);
                    }
                    //expand listview
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: " + e);
                }
                orderlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        p = position;
                        //set user name here
                       /* String alert1 = "Mobile No  :  " + orderList.get(p).getCustomer_id();
                        String alert2 = "Quintity           :  " + orderList.get(p).getBotttle_qty();
                        String alert3 = "Type               :  " + orderList.get(p).getBottle_type();
                        String alert4 = "Date               :  " + orderList.get(p).getOrder_date();
                        String alert5 = "Price              :  " + orderList.get(p).getBottle_price();
                        String alert6 = "Total Amount :  " + orderList.get(p).getTotal_cost();
                        String alert7 = "Customer Name :  " + orderList.get(p).getCustomer_name();
*/
                        final AlertDialog.Builder builder = new AlertDialog.Builder(admin_view_order.this, R.style.Theme_AppCompat_DayNight_Dialog);
                        builder.setTitle("Details");
                        //       builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4 + "\n" + alert5 + "\n" + alert6 + "\n" + alert7);
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                orderList.remove(p);
                                orderList.clear();
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font></font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                //select product for delete
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("tag", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void SetStandardGroups() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_order");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // final String headerTitle = dataSnapshot.getKey();
                //Toast.makeText(admin_view_order.this, "" + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                //    Log.e("TAG", headerTitle);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //   String child = (String) ds.getValue();
                    order child = ds.getValue(order.class);
                    orderList.add(child);

                   /*childItem.add("Price :" + String.valueOf(child.getBottle_price()));
                    childItem.add("Mobile No :" + String.valueOf(child.getCustomer_id()));
                    childItem.add("Quantity :" + String.valueOf(child.getBotttle_qty()));
                    childItem.add("Type :" + String.valueOf(child.getBottle_type()));
                    childItem.add("Order Date :" + String.valueOf0(child.getOrder_date()));
                    childItem.add("total cost :" + String.valueOf(child.getTotal_cost()));*/
                }
                for(int i=0;i<orderList.size();i++)
                {
                    if(orderList.get(i).getStatus().equals("1")) {
                        listDataHeader.add("            " + orderList.get(i).getOrder_date() + "        " + orderList.get(i).getCustomer_name() + "               " + orderList.get(i).getBotttle_qty() + "             " + "Ordered");
                    }

                    if(orderList.get(i).getStatus().equals("2")) {
                        listDataHeader.add("            " + orderList.get(i).getOrder_date() + "        " + orderList.get(i).getCustomer_name() + "               " + orderList.get(i).getBotttle_qty() + "             " + "Shipping");
                    }

                    if(orderList.get(i).getStatus().equals("3")) {
                        listDataHeader.add("            " + orderList.get(i).getOrder_date() + "        " + orderList.get(i).getCustomer_name() + "               " + orderList.get(i).getBotttle_qty() + "             " + "Delivered");
                    }

                }
                //
                // child data get
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Orer_details");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //String child = (String) ds.getValue();
                            orderdetails data1 = ds.getValue(orderdetails.class);
                            orderdetailList.add(data1);
                        }
                        for (int i = 0; i < orderList.size(); i++) {
                            try {
                                childItem = new ArrayList<>();
                                childItem.add("Pro_Name                Bottles                     Price");
                                String orderdata = orderList.get(i).getOrder_id();
                                //  Toast.makeText(admin_view_order.this, ""+orderdata, Toast.LENGTH_SHORT).show();
                                for (int j = 0; j < orderdetailList.size(); j++) {
                                    if (orderdetailList.get(j).getOrder_id().equals(orderdata)) {
                                        //  childItem.add("Price :" + String.valueOf(orderList.get(i).getOrder_id() + "        " + "Mobile No :" + String.valueOf(orderList.get(i).getCustomer_id())));
                                        //  childItem.add("Quantity :" + String.valueOf(orderList.get(i).getBotttle_qty() + "        " + "Status :" + String.valueOf(orderList.get(i).getStatus())));
                                        childItem.add(orderdetailList.get(j).getProduct_name() + "                            " + orderdetailList.get(j).getBotttle_qty() + "                             " + orderdetailList.get(j).getBottle_price());
                                        // childItem.add();
                                        //  childItem.add("Quantity  : "+orderdetailList.get(j).getBotttle_qty()+""+"Price  : "+orderdetailList.get(j).getBottle_price());
                                    }
                                    listDataChild.put(listDataHeader.get(i), childItem);
                                    counter++;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "onDataChange" + e);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                customExpandableListViewAdapter.notifyDataSetChanged();
                curvesLoader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void btnback(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), agent_login.class);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(i);
        finish();
    }
}
