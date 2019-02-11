package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.product_model.MyProductListAdapter;
import com.example.waterbottle.admin_agent_side.product_model.Product;
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
import java.util.Map;

public class admin_view_product extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Mohit";
    //the list values in the List of type hero
    ListView myListView;
    //list to store uploads data
    List<Product> productList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    //Firebase Url Get INstance
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    private DatabaseReference mDatabaseReference;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_admin_view_product);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();//displays the progress bar


        //initializing objects

        productList = new ArrayList<>();
        myListView = (ListView) findViewById(R.id.productlistview);


        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Product_data");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    progressDialog.dismiss();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Product upload = postSnapshot.getValue(Product.class);
                        productList.add(upload);
                        //arrayList1.add(postSnapshot.getKey());
                    }
                    String[] uploads = new String[productList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = productList.get(i).getProduct_name();
                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
                    }
                    //displaying it to list
                    final MyProductListAdapter adapter = new MyProductListAdapter(getApplicationContext(), R.layout.product_listview, productList);
                    myListView.setAdapter(adapter);

                        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(admin_view_product.this, "dfdfd", Toast.LENGTH_SHORT).show();
                            }
                        });
                    

                    //select product for delete
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: " + e);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        for (int i = 1; i < arrayList.size(); i++) {
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
                //Logout From cURRENT User

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                if (fAuth != null)
                {
                    Intent i=new Intent(getApplicationContext(),agent_login.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(this, "Cant Logout", Toast.LENGTH_SHORT).show();
                }

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
                //set all Client display activity
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

                String pnm = edtnm.getText().toString();
                String price = edtprice.getText().toString();
                String detail = edtdetail.getText().toString();


                Map<String, String> users = new HashMap<>();
                users.put("Product_name", pnm);
                users.put("Product_Price", price);
                users.put("Product_detail", detail);
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
                //set all Client display activity
                Intent i = new Intent(getApplicationContext(), admin_dashboard.class);
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
                //set all Client display activity
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
                String nm = edtnm.getText().toString();
                String mob = edtmob.getText().toString();
                String add = edtadd.getText().toString();
                String add2 = edtadd2.getText().toString();
                String qrcode = edtbarcode.getText().toString();

                Map<String, String> users = new HashMap<>();
                users.put("Customer_name", nm);
                users.put("Mobile_number", mob);
                users.put("Address ", add);
                users.put("Local_address ", add2);
                users.put("Customer_qrcode", qrcode);
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