package com.example.waterbottle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.admin_agent_side.SlidingImage_Adapter;
import com.example.waterbottle.admin_agent_side.agent_add_bottle;
import com.example.waterbottle.admin_agent_side.product_model.Product;
import com.example.waterbottle.admin_agent_side.sliding_image;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
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

public class customer_order extends AppCompatActivity{

    //firebase

    List<sliding_image> slid_img;
    private DatabaseReference mDatabaseReference;
    List<Product> productList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    String s;

    //bottle details
    private static TextView tvdetails,tvprice;
    EditText edtbottle;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGESNAME= {"Bisleri","kaveri","Oxyri","Mini Bisleri"};
    private static final Integer[] IMAGES= {R.drawable.bta,R.drawable.na,R.drawable.nb,R.drawable.nc};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_customer_order);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

      //  tvtotalbottle = findViewById(R.id.);
        tvdetails = findViewById(R.id.tvbottledetails);
        tvprice=findViewById(R.id.tvprice);

        FirebaseApp.initializeApp(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Product_data");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                        Toast.makeText(getApplicationContext(), "" + uploads[i], Toast.LENGTH_SHORT).show();

                        mPager = (ViewPager) findViewById(R.id.pager);

                        mPager.setAdapter(new SlidingImage_Adapter(customer_order.this, slid_img));

                        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

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

                                sliding_image slid =new sliding_image();
                                String a=slid.getQry();
                                Toast.makeText(getApplicationContext(), "qty:"+a, Toast.LENGTH_SHORT).show();

                                tvdetails.setText(slid_img.get(position).getProduct_name());
                                tvprice.setText(slid_img.get(position).getProduct_Price());
                                //total();

                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {

                            }
                        });

                    }

                    //  Toast.makeText(customer_order.this, "askd" + arrayList1.size(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
//        fab.setOnClickListener(this);
        //    fab1.setOnClickListener(this);
        //   fab2.setOnClickListener(this);
        //   fab3.setOnClickListener(this);
        //   fab4.setOnClickListener(this);

    }


    public void addbottle(View view) {
        int a= Integer.parseInt(edtbottle.getText().toString());
        int b=1;
        a=b+a;

        edtbottle.setText(""+a);
        tvprice.setText("Total Bottles :"+a);

    }

    public void minbottle(View view) {
        //using button sub bottles
        int a= Integer.parseInt(edtbottle.getText().toString());
        if(a==0)
        {
            Toast.makeText(this, "No Bottle In Cart", Toast.LENGTH_SHORT).show();
        }
        else {
            int b = 1;
            a = a-b;
            //set bottle in textbox and edittextbox
            edtbottle.setText("" + a);
            tvprice.setText("Total Bottles :"+a);
        }
    }

    public void btnok(View view) {

        Firebase.setAndroidContext(this);
        Firebase ref;

        ref = new Firebase(url);
        //date of order
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        /*int b= Integer.parseInt(tvtotalbottle.getText().toString());
        a=a+b;*/

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Map<String, String> users = new HashMap<>();
        //users.put("Botttle_qty", String.valueOf(a));
        users.put("Bottle_type", tvdetails.getText().toString());
        users.put("bottle_price", tvprice.getText().toString());
        users.put("Order_date",date);
        //users.put("all_Total", String.valueOf(b));
        ref.child("Customer_order").push().setValue(users);

        Toast.makeText(this, "Order Successfully Delivered", Toast.LENGTH_SHORT).show();

    }
}

