package com.example.waterbottle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.waterbottle.admin_agent_side.Model.agent;
import com.example.waterbottle.admin_agent_side.agent_login;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class client_dashboard extends AppCompatActivity implements View.OnClickListener {
    // inside Activity

    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;

    //for  fab buttton
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    List<agent> agentList;

    //the listview
    ListView listView,listView1,listView2;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_client_dashboard);



        Date d = new Date();
        CharSequence s  = DateFormat.format("dd-MM-yyyy ", d.getTime());

        listView = (ListView) findViewById(R.id.listpadding);
        listView1 = (ListView) findViewById(R.id.listpadding1);
        listView2 = (ListView) findViewById(R.id.listpadding3);

        // Initializing a new String Array
        String[] fruits = new String[] {
                s.toString() +"                    $300",
                s.toString() +"                    $300",
                s.toString() +"                    $300",
                s.toString() +"                    $300",
                s.toString() +"                    $300",
                s.toString() +"                    $300",
        };


        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.<String>asList(fruits));
     //   final List<String> fruits_list1 = new ArrayList<String>(Arrays.<String>asList(price));
        List<String> combined = new ArrayList<String>();
        combined.addAll(fruits_list);
        //combined.addAll(fruits_list1);

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, combined);

        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);
        listView1.setAdapter(arrayAdapter);
        listView2.setAdapter(arrayAdapter);



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
                Intent i1=new Intent(this,customer_order.class);
                startActivity(i1);

                break;

            case R.id.fab3:
                Log.d("a", "Fab 3");
                Intent i=new Intent(this,client_view_product.class);
                startActivity(i);

                break;
            case R.id.fab4:
                //Logout From cURRENT User
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                if (fAuth != null)
                {
                    Intent iq=new Intent(getApplicationContext(), client_login.class);
                    startActivity(iq);
                    finish();
                }
                else {
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

            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);


            isFabOpen = true;
            Log.d("Raj", "open");
        }
    }
}
