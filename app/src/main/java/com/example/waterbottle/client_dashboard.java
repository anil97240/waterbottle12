package com.example.waterbottle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.admin_agent_side.Model.agent;
import com.example.waterbottle.client_side.client_model.Bottle_delivered;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class client_dashboard extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Mohit";
    // inside Activity

    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;
    List<agent> agentList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    ArrayList all = new ArrayList<String>();


    //the listview
    ListView listView, listView1, listView2;
    //database reference to get uploads data
    DatabaseReference mDatabaseReference;
    List<Bottle_delivered> bottle_deliveredList;
    Bottle_delivered upload;
    //for  fab buttton
    StringBuffer sb;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView tvhide, tvagenthide, tvproducthide, tvlogouthide;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_client_dashboard);
        tvhide = findViewById(R.id.tvhide);
        tvagenthide = findViewById(R.id.tvagenthide);
        tvproducthide = findViewById(R.id.tvhideproduct);
        tvlogouthide = findViewById(R.id.tvhidelogout);


        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy ", d.getTime());

        listView = (ListView) findViewById(R.id.listpadding);
        listView1 = (ListView) findViewById(R.id.listpadding1);
        listView2 = (ListView) findViewById(R.id.listpadding3);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);

        bottle_deliveredList = new ArrayList<>();
        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Bottle_delivered");
        Log.e(TAG, "onCreate: " + mDatabaseReference);
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    upload = postSnapshot.getValue(Bottle_delivered.class);
                    bottle_deliveredList.add(upload);


                }

                String[] uploads = new String[bottle_deliveredList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    String a = uploads[i] = bottle_deliveredList.get(i).getDelivry_date();
                    //Fore get Total
                    arrayList.add(bottle_deliveredList.get(i).getDelivry_date().toString()),bottle_deliveredList.get(i).getAmount_collected());
                    Log.e(TAG, "onDataChange: "+arrayList );








                }
                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView2.setAdapter(adapter);






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });


    }


    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                Log.d("a", "Fab 1");
                showCustomDialog();


                break;

            case R.id.fab2:
                //Toast.makeText(this, "log1", Toast.LENGTH_SHORT).show();
                Log.d("a", "Fab 2");
                Intent i1 = new Intent(this, customer_order.class);
                startActivity(i1);

                break;

            case R.id.fab3:
                Log.d("a", "Fab 3");
                Intent i = new Intent(this, client_view_product.class);
                startActivity(i);

                break;
            case R.id.fab4:
                //Logout From cURRENT User
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                if (fAuth != null) {
                    Intent iq = new Intent(getApplicationContext(), client_login.class);
                    startActivity(iq);
                    finish();
                } else {
                    Toast.makeText(this, "Cant Logout", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    private void showCustomDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(client_dashboard.this);


        View view = getLayoutInflater().inflate(R.layout.dialong_customer_profile, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();

        //get all edittext in dialog_customer_add
      /*  final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.tvaddress);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview=view.findViewById(R.id.imgview);*/


//client profile
        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
             /*   Intent i = new Intent(getApplicationContext(), Admin_view_all_client.class);
                startActivity(i);*/

            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add barcode scann code herer
                //Toast.makeText(Admin_view_all_client.this, "barcode scanner", Toast.LENGTH_SHORT).show();
            }
        });


        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    private void animateFAB() {
        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);

            tvhide.startAnimation(fab_close);
            tvagenthide.startAnimation(fab_close);
            tvproducthide.startAnimation(fab_close);
            tvlogouthide.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);

            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);


            tvhide.startAnimation(fab_open);
            tvagenthide.startAnimation(fab_open);
            tvproducthide.startAnimation(fab_open);
            tvlogouthide.startAnimation(fab_open);


            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);


            isFabOpen = true;
            Log.d("Raj", "open");
        }
    }
}
