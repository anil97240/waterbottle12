package com.example.waterbottle;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class agent_view_client_details extends AppCompatActivity {
    String userName, userName1;
    int adata = 0;

    RadioGroup rdgroup;
    View coordinatorLayout;
    RadioButton rdocash, rdoother;
    client_data_listadepter adapter;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    int datapending = 0;
    ListView clietlist;
    EditText edtamount, edtpendding;
    TextView tvtotaldata, tvpendingamout, tvtotalamount;
    CurvesLoader curvesLoader;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    List<cliet_data> deliver_orderList;
    List<cliet_data> deliver_orderList2;
    private String TAG = "data";
    ArrayList arrary = new ArrayList<>();
    ArrayList arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_agent_view_client_details);

        coordinatorLayout = findViewById(R.id.layoutorder);
        tvtotalamount = findViewById(R.id.tvtotalamount);
        edtamount = findViewById(R.id.edtamount);
        edtpendding = findViewById(R.id.edtpendding);

        curvesLoader = findViewById(R.id.curvesLoader);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("qrcode");
            userName1 = extras.getString("cnm");

            // and get whatever type user account id is
            // edtbarcode.setText(userName.toString());
        }

        rdgroup = findViewById(R.id.rdgroup);


        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

     /*   FirebaseUser user =mAuth.getCurrentUser();
        String provider = user.getProviders().get(0);*/
        deliver_orderList = new ArrayList<>();
        deliver_orderList2 = new ArrayList<>();
        clietlist = (ListView) findViewById(R.id.listviewclietdata);

        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        cliet_data upload = postSnapshot.getValue(cliet_data.class);
                        deliver_orderList.add(upload);
                        arrayList.add(postSnapshot.getKey());

                    }
                    String[] uploads = new String[deliver_orderList.size()];
                    String[] qrcode = new String[deliver_orderList.size()];
                    for (int i = 0; i < uploads.length; i++) {

                        uploads[i] = deliver_orderList.get(i).getAgent_email();
                        qrcode[i] = deliver_orderList.get(i).getQR_code();

                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
                    }
                    curvesLoader.setVisibility(View.GONE);
                    //displaying it to list

                    for (int i = 0; i < qrcode.length; i++) {

                        if (qrcode[i].contains(userName)) {
                            deliver_orderList2.add(deliver_orderList.get(i));

                        }
                        adapter = new client_data_listadepter(getApplicationContext(), R.layout.customer_amout_listview, deliver_orderList2);
                        clietlist.setAdapter(adapter);
                        clietlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                final int p = position;

                                final AlertDialog.Builder builder = new AlertDialog.Builder(agent_view_client_details.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                builder.setMessage("Are You Sure ?");
                                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Delete</font></font>"), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {
                                        mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                        //  Toast.makeText(Admin_view_all_client.this, "data remove", Toast.LENGTH_SHORT).show();
                                        deliver_orderList.remove(p);
                                        deliver_orderList.clear();
                                        deliver_orderList2.clear();
                                        arrayList.clear();
                                        adapter.notifyDataSetChanged();
                                        //do things
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


                    }
                    //get amout total for pending and collected
                    // adata = adata + Integer.parseInt(deliver_orderList2.getAmount_collected());

                    int a = deliver_orderList2.size() - 1;
                    datapending = Integer.parseInt(deliver_orderList2.get(a).getPadding_amount());

                    //  datapending= Integer.parseInt(cliet_data.getPadding_amount());
                  /*  tvtotaldata.setText("Collected Amount:"+adata);
                    tvpendingamout.setText("Pending Amout:"+datapending);*/
                    tvtotalamount.setText("" + datapending);
                    edtpendding.setText("" + datapending);
                    //select product for delete
                } catch (Exception e) {
                    Log.e(TAG, "New: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        edtamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start >= 0) {
                    try {
                        int a;
                        int b;
                        b = Integer.parseInt(s.toString());
                        a = datapending;
                        a = a - b;
                        a = a + 0;
                        edtpendding.setText("" + a);
                    } catch (Exception e) {
                        edtpendding.setText("" + datapending);
                    }
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    public void backpress(View view) {
        super.onBackPressed();
    }

    public void adddata(View view) {


        int a = 0, t = 0, p = 0;
        String s, s1;
        s = edtamount.getText().toString();
        s1 = edtpendding.getText().toString();

        if (!s.equals("")) {
            a = Integer.parseInt(s);
        }
        if (!s1.equals("")) {
            p = Integer.parseInt(s1);
        }
        t = a + p;
        // mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount_agent");
        if (edtamount.getText().toString().equals("")) {
            edtamount.setError("Enter Some Amount");
        } else {
            Firebase ref;
            ref = new Firebase(url);
            String date;
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            Map<String, String> users = new HashMap<>();

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            users.put("Customer_name", userName1);
            users.put("QR_code", userName);
            users.put("Agent_email", mFirebaseUser.getEmail().toString());
            users.put("Amount_total", String.valueOf(t));
            users.put("Amount_collected", String.valueOf(a).toString());
            users.put("Padding_amount", String.valueOf(p));
            users.put("Collected_Date", date.toString());
            users.put("Payment_Method", "Cash");
            ref.child("collected_amount").push().setValue(users);
            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Data Added", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            //clear editext and Textview
            edtpendding.setText("");
            edtamount.setText("");
            tvtotalamount.setText("");
            //for a hide a keyboard
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(edtamount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            try {
                //clear all data in listview
                deliver_orderList.clear();
                deliver_orderList2.clear();
                //update adepter data and redisplay
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e(TAG, "Adepter Notify: " + e);
            }
        }
    }
}