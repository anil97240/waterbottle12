package com.example.waterbottle.admin_agent_side;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.waterbottle.admin_agent_side.Model.CustomExpandableorderlist;
import com.example.waterbottle.admin_agent_side.Model.deliver_order;
import com.example.waterbottle.admin_agent_side.Model.deliver_order_adepter;
import com.example.waterbottle.admin_agent_side.Model.order;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class order_delivered extends AppCompatActivity {

    private static final String TAG = "Mohit";
    int p;
    CurvesLoader curvesLoader;
    //the list values in the List of type hero
    ListView delivryListview;



    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    //list to store uploads data
    List<deliver_order> deliver_orderList;
    List<String> deliver_orderList1;

    ArrayList arrayList = new ArrayList<String>();
    deliver_order_adepter adapter;
    //Firebase Url Get INstance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    ExpandableListView expandableListView;
    CustomExpandableorderlist customExpandableListViewAdapter;
    //expandlistview
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_order_delivered);
        curvesLoader = findViewById(R.id.curvesLoader);

        SharedPreferences prfs = getSharedPreferences("auth", MODE_PRIVATE);
        String authname = prfs.getString("authname", "");
        expandableListView = findViewById(R.id.expandlist);
        deliver_orderList1 = new ArrayList<String>();
        if (authname == "") {
            Intent intent = new Intent(order_delivered.this, agent_login.class);
            SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(intent);
            finish();
        }
        else
            {
            }
        SetStandardGroups();
        customExpandableListViewAdapter = new CustomExpandableorderlist(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(customExpandableListViewAdapter);
        //initializing objects
        /* deliver_orderList = new ArrayList<>();*/
        delivryListview = (ListView) findViewById(R.id.ordercompletelistview);
        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        deliver_order upload = postSnapshot.getValue(deliver_order.class);
                        deliver_orderList.add(upload);
                        arrayList.add(postSnapshot.getKey());
                    }

                    String[] uploads = new String[deliver_orderList.size()];
                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = deliver_orderList.get(i).getQR_code();
                        Log.e(TAG, "onDataChange: " + uploads[i]);
                    }
                    //displaying it to list
                    adapter = new deliver_order_adepter(getApplicationContext(), R.layout.order_complete_listview, deliver_orderList);
                    delivryListview.setAdapter(adapter);
                    delivryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            p = position;
                            String alert1 = "Agent Email   :  " + deliver_orderList.get(p).getAgent_email();
                            String alert4 = "Total Amount        :  " + deliver_orderList.get(p).getAmount_total();
                            String alert2 = "Customer QR        :  " + deliver_orderList.get(p).getQR_code();
                            String alert3 = "Date                        :  " + deliver_orderList.get(p).getCollected_Date();
                            String alert5 = "Pending Amount   :  " + deliver_orderList.get(p).getPadding_amount();
                            String alert6 = "Collected Amount :  " + deliver_orderList.get(p).getAmount_collected();
                            String alert7 = "Payment Method    :  " + deliver_orderList.get(p).getPayment_Method();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(order_delivered.this, R.style.Theme_AppCompat_DayNight_Dialog);
                            builder.setTitle("Details");
                            builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4 + "\n" + alert5 + "\n" + alert6 + "\n" + alert7);

                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                    deliver_orderList.remove(p);
                                    deliver_orderList.clear();
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
                } catch (Exception e) {
                    Log.e(TAG, "New: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void SetStandardGroups() {
        deliver_orderList=new ArrayList<>();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> childItem;
                int counter = 0;
                // final String headerTitle = dataSnapshot.getKey();
              //  Toast.makeText(admin_view_order.this, "" + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                //    Log.e("TAG", headerTitle);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //   String child = (String) ds.getValue();
                    deliver_order child = ds.getValue(deliver_order.class);
                    deliver_orderList.add(child);
                    listDataHeader.add("        "+child.getCustomer_name()+"                             "+child.getAmount_collected()+"                      "+child.getPadding_amount());
                   /* childItem.add("Price :" + String.valueOf(child.getBottle_price()));
                    childItem.add("Mobile No :" + String.valueOf(child.getCustomer_id()));
                    childItem.add("Quantity :" + String.valueOf(child.getBotttle_qty()));
                    childItem.add("Type :" + String.valueOf(child.getBottle_type()));
                    childItem.add("Order Date :" + String.valueOf(child.getOrder_date()));
                    childItem.add("total cost :" + String.valueOf(child.getTotal_cost()));*/
                }
                for(int i=0;i<deliver_orderList.size();i++)
                {
                    childItem = new ArrayList<>();
                    childItem.add("Agent Email :" + String.valueOf(deliver_orderList.get(i).getAgent_email()+"     "));
                    childItem.add("Collected_date :" + String.valueOf(deliver_orderList.get(i).getCollected_Date()+"     "+"Method :" + String.valueOf(deliver_orderList.get(i).getPayment_Method())));
                    childItem.add("Total Amount :" + String.valueOf(deliver_orderList.get(i).getAmount_total()+"       C_QR :"+deliver_orderList.get(i).getQR_code()));
                    listDataChild.put(listDataHeader.get(counter), childItem);
                    counter++;
                }
                customExpandableListViewAdapter.notifyDataSetChanged();
                curvesLoader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void gotodash(View view) {
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
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