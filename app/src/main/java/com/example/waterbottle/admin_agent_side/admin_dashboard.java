package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.MyListAdapter;
import com.example.waterbottle.admin_agent_side.Model.agent;

import java.util.ArrayList;
import java.util.List;

public class admin_dashboard extends AppCompatActivity implements View.OnClickListener{

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    List<agent> agentList;

    //the listview
    ListView listView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_admin_dashboard);
        agentList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
    //listview add values using arraaylist
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));




        //initializing objects



        MyListAdapter adapter = new MyListAdapter(this, R.layout.my_custom_list, agentList);

        listView.setAdapter(adapter);


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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Log.d("Raj", "Fab 1");
                showCustomDialog();
                break;
            case R.id.fab2:
                Log.d("Raj", "Fab 2");
                showAgentDialog();
                break;

            case R.id.fab3:
                Log.d("Raj", "Fab 3");
           //     Toast.makeText(this, "fab 3", Toast.LENGTH_SHORT).show();
                showProductDialog();
                break;
                
            case R.id.fab4:

                Log.d("Raj", "Fab 4");
                break;

        }
}

    private void showProductDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(admin_dashboard.this);

        LayoutInflater inflater = LayoutInflater.from(admin_dashboard.this);
        View view = inflater.inflate(R.layout.dialong_product_add, null);
        builder.setView(view);


        //get all edittext in dialog_customer_add
      /*  final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.tvaddress);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview=view.findViewById(R.id.imgview);*/


        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnallproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                //display all product page

                Intent i = new Intent(getApplicationContext(),admin_view_product.class);
                startActivity(i);
                dialog.dismiss();
            }
        });


        view.findViewById(R.id.btnupload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for image upload and Display image

            }
        });

        view.findViewById(R.id.btnaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

    }

    private void showAgentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(admin_dashboard.this);

        LayoutInflater inflater = LayoutInflater.from(admin_dashboard.this);
        View view = inflater.inflate(R.layout.dialong_agent_add, null);
        builder.setView(view);


        //get all edittext in dialog_customer_add
      /*  final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.tvaddress);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview=view.findViewById(R.id.imgview);*/


        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                //Intent i = new Intent(getApplicationContext(),admin_dashboard.class);
                //startActivity(i);
                dialog.dismiss();
            }
        });


        view.findViewById(R.id.btnupload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for image upload and Display image

            }
        });

        view.findViewById(R.id.btnaddagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });







    }

    private void showCustomDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(admin_dashboard.this);

        LayoutInflater inflater = LayoutInflater.from(admin_dashboard.this);
        View view = inflater.inflate(R.layout.dialog_customer_add, null);
        builder.setView(view);


        //get all edittext in dialog_customer_add
      /*  final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.tvaddress);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview=view.findViewById(R.id.imgview);*/


        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                Intent i = new Intent(getApplicationContext(), Admin_view_all_client.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add barcode scann code herer
                //Toast.makeText(Admin_view_all_client.this, "barcode scanner", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnupload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code for image upload and Display image

            }
        });

        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


        view.findViewById(R.id.tvuploadfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for image selec code here

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
