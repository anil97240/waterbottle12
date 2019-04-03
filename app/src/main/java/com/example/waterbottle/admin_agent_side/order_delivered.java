package com.example.waterbottle.admin_agent_side;

import android.app.ProgressDialog;
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
import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.CustomExpandableListAdapter;
import com.example.waterbottle.admin_agent_side.Model.CustomExpandableorderlist;
import com.example.waterbottle.admin_agent_side.Model.ExpandableListDataPump;
import com.example.waterbottle.admin_agent_side.Model.Expandabledatadeliver;
import com.example.waterbottle.admin_agent_side.Model.deliver_order;
import com.example.waterbottle.admin_agent_side.Model.deliver_order_adepter;
import com.firebase.client.Firebase;
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

    //list to store uploads data
    List<deliver_order> deliver_orderList;
    ArrayList arrayList = new ArrayList<String>();
    deliver_order_adepter adapter;
    //Firebase Url Get INstance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    ExpandableListView expandableListView;
    //expandlistview
    CustomExpandableorderlist expandableListAdapter;
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

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = Expandabledatadeliver.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableorderlist(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + "List Expanded.",Toast.LENGTH_SHORT).show();
                //curvesLoader.setVisibility(View.GONE);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + "List Collapsed.",Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), expandableListTitle.get(groupPosition) + " -> " + expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (authname == "") {
            Intent intent = new Intent(order_delivered.this, agent_login.class);
            SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(intent);
            finish();
        } else {

        }
        //initializing objects
        deliver_orderList = new ArrayList<>();
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
                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
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
    public void gotodash(View view) {
        super.onBackPressed();

    }
}