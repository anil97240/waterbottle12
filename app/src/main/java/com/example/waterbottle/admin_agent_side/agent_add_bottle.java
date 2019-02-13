package com.example.waterbottle.admin_agent_side;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.product_model.Product;
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

public class agent_add_bottle extends AppCompatActivity {
    public static final Integer[] IMAGES = {R.drawable.bta, R.drawable.na, R.drawable.nb, R.drawable.nc};
    private static final String[] IMAGESNAME = {"Bisleri", "kaveri", "Oxyri", "Mini Bisleri"};
    //bottle details
    private static TextView tvdetails, tvtotalbottle;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public String date_of_birth;
    public String full_name;
    public String nickname;
    int bottleprice = 0;
    EditText edtbottle;
    //save in firebase
    String userName;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    String s;
    Product pro;

    List<sliding_image> slid_img;
    List<Product> productList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    ArrayList arrprice = new ArrayList<String>();
    String[] uploads;
    String[] adddata;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_agent_add_bottle);

        tvtotalbottle = findViewById(R.id.txttotalbottle2);

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

                        tvdetails = findViewById(R.id.tvbottledetails);

                        mPager.setAdapter(new SlidingImage_Adapter(agent_add_bottle.this, slid_img));

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

                                sliding_image slid = new sliding_image();
                                String a = slid.getQry();
                                Toast.makeText(getApplicationContext(), "qty:" + a, Toast.LENGTH_SHORT).show();

                                tvdetails.setText(slid_img.get(position).getProduct_name());
                                tvtotalbottle.setText(slid_img.get(position).getProduct_Price());
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

                    Toast.makeText(agent_add_bottle.this, "askd" + arrayList1.size(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
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
        Toast.makeText(this, "" + userName, Toast.LENGTH_SHORT).show();

    }

    private void total() {

        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }
//get all product from firebase

    public void btnok(View view) {
        Firebase.setAndroidContext(this);
        Firebase ref;

        ref = new Firebase(url);

        /*int b= Integer.parseInt(tvtotalbottle.getText().toString());
        a=a+b;*/
        //set date for database
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        //data add in fire base

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Map<String, String> users = new HashMap<>();
        //users.put("Botttle_qty", String.valueOf(a));
        users.put("QR_code", userName);
        users.put("Order_complate_date", date);
        users.put("Bottle_type", tvdetails.getText().toString());
        users.put("bottle_price", tvtotalbottle.getText().toString());
        // users.put("all_Total", String.valueOf(b));

        ref.child("Bottle_delivered").push().setValue(users);

        Toast.makeText(this, "Order Successfully Delivered", Toast.LENGTH_SHORT).show();

    }

    public void goback(View view) {
        Intent intent = new Intent(this, agent_barcode.class);
        startActivity(intent);
        finish();
    }
}

