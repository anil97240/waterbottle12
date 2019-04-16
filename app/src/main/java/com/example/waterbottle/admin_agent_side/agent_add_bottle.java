package com.example.waterbottle.admin_agent_side;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.MainActivity;
import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.order;
import com.example.waterbottle.admin_agent_side.Model.order_adepter;
import com.example.waterbottle.admin_agent_side.product_model.Product;
import com.example.waterbottle.agent_view_client_details;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class agent_add_bottle extends AppCompatActivity {


    //bottle details
    private static TextView tvdetails;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    List<order> orderList;
    List<order> orderList2;
    static String as;
    static int priceofcon;
    static TextView tvprice;
    static TextView tv_totalcost;
    static TextView tvqty;
    int lasin;
    String userName;
    String cusnm;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    String s;
    Product pro;
    sliding_image slid = new sliding_image();
    List<sliding_image> slid_img;
    View coordinatorLayout;
    ArrayList arrayList1 = new ArrayList<String>();
    ArrayList arrayListkey = new ArrayList<String>();
    ArrayList arrayListkey2 = new ArrayList<String>();
    View coordinatorLayout1;

    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //will hide the title

        getSupportActionBar().hide();

        // hide the title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        coordinatorLayout1 = findViewById(R.id.relativeLayout);

        setContentView(R.layout.activity_agent_add_bottle);
        orderList=new ArrayList<>();
        orderList2=new ArrayList<>();
        tvqty=findViewById(R.id.tvqty);
        tvdetails = findViewById(R.id.tvbottledetails);
        tvprice = findViewById(R.id.tvprice);
        tv_totalcost=findViewById(R.id.tv_totalcost);

        FirebaseApp.initializeApp(this);
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // User Name

        String Agent_name = mFirebaseUser.getDisplayName();

        // User ID
        mFirebaseUser.getUid();

        // Email-ID

        mFirebaseUser.getEmail();

        //User-Profile (if available)

        mFirebaseUser.getPhotoUrl();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Product_data");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                try {
                    slid_img = new ArrayList<>();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        sliding_image list = postSnapshot.getValue(sliding_image.class);
                        slid_img.add(list);
                        arrayList1.add(postSnapshot.getKey());
                    }
                    String[] uploads = new String[slid_img.size()];
                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = slid_img.get(i).getImage();
                    }

                    for (int i = 0; i < uploads.length; i++) {
                        String s = (uploads[i]);

                        mPager = (ViewPager) findViewById(R.id.pager);

                        tvdetails = findViewById(R.id.tvbottledetails);
                        mPager.setAdapter(new SlidingImage_Adapter(agent_add_bottle.this, slid_img));
                        tvdetails.setText(slid_img.get(0).getProduct_name());
                        tvprice.setText("" + slid_img.get(0).getProduct_Price());
                        priceofcon= Integer.parseInt(tvprice.getText().toString());

                        final CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

                        indicator.setViewPager(mPager);

                        final float density = getResources().getDisplayMetrics().density;

                        //Set circle indicator radius
                        indicator.setRadius(8 * density);

                        NUM_PAGES = uploads.length;
                        // Auto start of viewpager
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;

                                }
                                mPager.setCurrentItem(currentPage++, true);
                            }
                        };
                        // Pager listener over indicator
                        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(int position) {
                                currentPage = position;
                                tvdetails.setText(slid_img.get(position).getProduct_name());
                                tvprice.setText(""+slid_img.get(position).getProduct_Price());
                                priceofcon= Integer.parseInt(tvprice.getText().toString());
                            }
                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {
                            }
                            @Override
                            public void onPageScrollStateChanged(int pos) {
                            }
                        });
                    }
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        pro = new Product();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("qrcode");
        }

        Bundle extras2 = getIntent().getExtras();
        if (extras != null) {
            cusnm = extras.getString("cnm");
        }

    }
    //get all product from firebase
    //add dialog

  /*  private void dataadd() {
        Snackbar snackbar12 = Snackbar.make(coordinatorLayout1, "Order Successfully.", Snackbar.LENGTH_SHORT);
        snackbar12.show();

    }*/

    public void goback(View view) {

        Intent intent = new Intent(this, agent_barcode.class);
        startActivity(intent);
        finish();

    }
    //min bottles for
    public static void min() {

        as = SlidingImage_Adapter.data.toString();

        int data = Integer.parseInt(tvqty.getText().toString());

        if (data==0)
        {

        } else
            {

            int b = Integer.parseInt(tvqty.getText().toString());
            b = b - 1;
            int lastprice;//= Integer.parseInt(slid.getProduct_Price());
            lastprice= Integer.parseInt(tv_totalcost.getText().toString());
            int adata= Integer.parseInt(tvprice.getText().toString());
            lastprice=lastprice-adata;
            tvqty.setText("" + b);
            tv_totalcost.setText(""+lastprice);

            }
    }
    //adding bottles
    public static void add() {
        sliding_image slid=new sliding_image();
        as = SlidingImage_Adapter.data.toString();

        int tdata;
        tdata = Integer.parseInt(as);

        if (as.equals("0")) {

        } else {

            int a = Integer.parseInt(as);
            if (a == 0) {

                int b = Integer.parseInt(tvqty.getText().toString());
                b = b + 0;
                tvqty.setText("" + b+" 0");
                tvqty.setText("" + b);
            }
            else
                {
                int b = Integer.parseInt(tvqty.getText().toString());
                b = b + 1;
                int lastprice;
                lastprice= Integer.parseInt(tv_totalcost.getText().toString());//= Integer.parseInt(slid.getProduct_Price());
                int adata= Integer.parseInt(tvprice.getText().toString());
                lastprice=adata+lastprice;
                tvqty.setText("" + b);
                tv_totalcost.setText(""+lastprice);
                }
        }
    }
    public void btnadd(View view) {
        //firebase data add
        adddata();
    }
    private void adddata() {
        Firebase ref;
        ref = new Firebase(url);
        String date;
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Map<String, String> users = new HashMap<>();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String data1 = SlidingImage_Adapter.data.toString();
        String qty=tvqty.getText().toString();
        if (qty.equals("0")) {
            Toast.makeText(agent_add_bottle.this, "No Bottles In Cart", Toast.LENGTH_SHORT).show();
        }
        else
        {
            users.put("Botttle_price", tvprice.getText().toString());
            users.put("Agent_email", mFirebaseUser.getEmail());
            users.put("Botttles", tvqty.getText().toString());
            users.put("Delivry_date", date);
            users.put("QR_code", userName);
            users.put("Customer_name",cusnm);
            users.put("Total_amount", tv_totalcost.getText().toString());
            users.put("Bottle_type", tvdetails.getText().toString());
            ref.child("Bottle_delivered").push().setValue(users);
            updatedata();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(this, "Order Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void updatedata()
    {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_order");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        order upload = postSnapshot.getValue(order.class);
                        orderList.add(upload);
                        arrayListkey.add(postSnapshot.getKey());
                    }
                    // curvesLoader.setVisibility(View.GONE);
                    String[] uploads = new String[orderList.size()];
                    //Toast.makeText(agent_add_bottle.this, ""+userName, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < uploads.length; i++) {
                      //  Toast.makeText(agent_add_bottle.this, ""+orderList.get(i).getCustomer_qrcode(), Toast.LENGTH_SHORT).show();
                        // uploads[i] = orderList.get(i).getCustomer_name();
                        if (orderList.get(i).getCustomer_qrcode().equals(userName))
                        {
                            orderList2.add(orderList.get(i));
                            arrayListkey2.add(arrayListkey.get(i));
                            Toast.makeText(agent_add_bottle.this, "data in", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lasin=orderList2.size()-1;
                    Firebase ref;
                    ref = new Firebase(url);
                    Map<String, String> users = new HashMap<>();
                    users.put("Botttle_qty", orderList2.get(lasin).getBotttle_qty());
                    users.put("Customer_id", orderList2.get(lasin).getCustomer_id());
                    users.put("Customer_name", orderList2.get(lasin).getCustomer_name());
                    users.put("Order_date",orderList2.get(lasin).getOrder_date());
                    users.put("Order_id", orderList2.get(lasin).getOrder_id());
                    users.put("Status","3");
                    users.put("total_cost",orderList2.get(lasin).getTotal_cost());
                    ref.child("Customer_order").child(arrayListkey.get(lasin).toString()).setValue(users);

                    Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();
                    //displaying it to list
                    
                    //expand listview
                } catch (Exception e) {
                    Log.e("tag", "onDataChange: " + e);
                }
                //select product for delete
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("tag", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    //go to  back
    public void backpress(View view)
    {
        Intent i=new Intent(getApplicationContext(), agent_barcode.class);
        startActivity(i);
        finish();
    }

    public void view_client_details(View view)
    {
        Intent i1 = new Intent(getApplicationContext(), agent_view_client_details.class);
        i1.putExtra("qrcode",userName);
        i1.putExtra("cnm",cusnm);
        startActivity(i1);
    }
}

