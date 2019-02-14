package com.example.waterbottle.admin_agent_side;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.deliver_order;
import com.example.waterbottle.admin_agent_side.Model.deliver_order_adepter;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class order_delivered extends AppCompatActivity {

    private static final String TAG = "Mohit";

    //the list values in the List of type hero
    ListView delivryListview;

    //list to store uploads data
    List<deliver_order> deliver_orderList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();

    //Firebase Url Get INstance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";

    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_order_delivered);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();//displays the progress bar


        //initializing objects

        deliver_orderList = new ArrayList<>();
        delivryListview = (ListView) findViewById(R.id.ordercompletelistview);


        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Bottle_delivered");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    progressDialog.dismiss();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        deliver_order upload = postSnapshot.getValue(deliver_order.class);
                        deliver_orderList.add(upload);

                    }
                    String[] uploads = new String[deliver_orderList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = deliver_orderList.get(i).getQR_code();

                        Log.e(TAG, "onDataChange: " + uploads[i].toString());

                    }
                    //displaying it to list
                    deliver_order_adepter adapter = new deliver_order_adepter(getApplicationContext(), R.layout.order_complete_listview, deliver_orderList);

                    delivryListview.setAdapter(adapter);

                    delivryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
}