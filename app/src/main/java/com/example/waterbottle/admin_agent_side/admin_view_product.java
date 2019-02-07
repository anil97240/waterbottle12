package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.product_model.product;
import com.example.waterbottle.admin_agent_side.product_model.productlistadepter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class admin_view_product extends AppCompatActivity implements View.OnClickListener{


    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    List<product> productList;
    ArrayList arrayList = new ArrayList<String>();
    //the listview
    ListView listView;
    String url="https://waterbottle12-e6aa9.firebaseio.com/";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_admin_view_product);


        productList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView1);


       Firebase.setAndroidContext(this);

        Firebase ref;
        String url="https://waterbottle12-e6aa9.firebaseio.com/Product_data";
        ref = new Firebase(url);
        //DatabaseReference myRef = database.getReference("market");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //userlist.datas(String.valueOf(dsp.getKey())); //add result into array list
                //    String key1 = dsp.getKey();
                    arrayList.add(dsp.getValue().toString());
                   String s= String.valueOf(arrayList.get(0));
                   productList.add(new product(R.drawable.logo, 0, s, "1000", "50 L"));

               }
              /*  String[] uploads = new String[arrayList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = arrayList.get(i);
                }*/

               productlistadepter adapter = new productlistadepter(getApplicationContext(),R.layout.product_listview, productList);

                listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





        //add product list in listview


     //   String s= String.valueOf(arrayList.get(0));
     //   Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
   /*     productList.add(new product(R.drawable.logo, 0, "abc","1000","50 L"));
        productList.add(new product(R.drawable.logo, 0, "abc","1000","50 L"));
        productList.add(new product(R.drawable.logo, 0, "abc","1000","50 L"));*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        for(int i=1;i<arrayList.size();i++) {
            Toast.makeText(admin_view_product.this, "" + arrayList.get(i), Toast.LENGTH_SHORT).show();
        }


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
                //Toast.makeText(this, "log1", Toast.LENGTH_SHORT).show();
                Log.d("a", "Fab 2");
                showAgentDialog();
                break;

            case R.id.fab3:

                Log.d("a", "Fab 3");
                showProductDialog();
                break;
            case R.id.fab4:
                Intent i=new Intent(this,agent_login.class);
                startActivity(i);

                Log.d("a", "Fab 4");
                break;

        }
    }
//for product add dialong method

    private void showProductDialog() {




        AlertDialog.Builder builder = new AlertDialog.Builder(admin_view_product.this);

        View view = getLayoutInflater().inflate(R.layout.dialong_product_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();


        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtprice = view.findViewById(R.id.edtprice);
        final EditText edtdetail = view.findViewById(R.id.edtdetail);



        view.findViewById(R.id.btnallproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                //display all product page

            }
        });



        view.findViewById(R.id.btnaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase ref;

                ref = new Firebase(url);

                //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                // DatabaseReference usersRef = ref.child("users");

                String pnm=edtnm.getText().toString();
                String price=edtprice.getText().toString();
                String detail=edtdetail.getText().toString();


                Map<String, String> users = new HashMap<>();
                users.put("Product_name",pnm);
                users.put("Product_Price",price);
                users.put("Product_detail",detail);
                ref.child("Product_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });


    }
//for agent add
    private void showAgentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(admin_view_product.this);

        View view = getLayoutInflater().inflate(R.layout.dialong_agent_add, null);
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

      //for agent add

        view.findViewById(R.id.btnallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                Intent i = new Intent(getApplicationContext(),admin_dashboard.class);
                startActivity(i);

            }
        });



        view.findViewById(R.id.btnaddagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });







    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(admin_view_product.this);

        View view = getLayoutInflater().inflate(R.layout.dialog_customer_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();


        //get all edittext in dialog_customer_add
        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.edtname);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        //final ImageView imgview=view.findViewById(R.id.imgview);


        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                Intent i = new Intent(getApplicationContext(), Admin_view_all_client.class);
                startActivity(i);
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
                Firebase ref;

                ref = new Firebase(url);

                //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                // DatabaseReference usersRef = ref.child("users");
                String nm=edtnm.getText().toString();
                String mob=edtmob.getText().toString();
                String add=edtadd.getText().toString();
                String add2=edtadd2.getText().toString();
                String qrcode=edtbarcode.getText().toString();

                Map<String, String> users = new HashMap<>();
                users.put("Customer_name",nm);
                users.put("Mobile_number",mob);
                users.put("Address ",add);
                users.put("Local_address ",add2);
                users.put("Customer_qrcode",qrcode);
                ref.child("Customer_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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